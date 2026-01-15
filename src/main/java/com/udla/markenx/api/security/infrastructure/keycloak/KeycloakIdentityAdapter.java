package com.udla.markenx.api.security.infrastructure.keycloak;

import com.udla.markenx.api.security.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.security.domain.exceptions.EmailAlreadyExistsException;
import com.udla.markenx.api.security.domain.exceptions.UserNotFoundInIdentityProviderException;
import com.udla.markenx.api.security.infrastructure.keycloak.dtos.KeycloakUserResponse;
import com.udla.markenx.api.security.infrastructure.keycloak.properties.IdentityProvisioningProperties;
import com.udla.markenx.api.security.infrastructure.keycloak.properties.KeycloakAdminProperties;
import com.udla.markenx.api.security.infrastructure.web.dtos.CreateUserRequest;
import com.udla.markenx.api.security.infrastructure.web.dtos.UpdateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KeycloakIdentityAdapter implements ExternalIdentityPort {

    private static final String CREATE_USER_URI = "/admin/realms/{realm}/users";
    private static final String SEARCH_BY_USERNAME_URI = "/admin/realms/{realm}/users?username={username}&exact=true";
    private static final String UPDATE_USER_URI = "/admin/realms/{realm}/users/{userId}";
    private static final String RESET_PASSWORD_URI = "/admin/realms/{realm}/users/{userId}/reset-password";
    private static final String REALM_ROLE_URI = "/admin/realms/{realm}/roles/{roleName}";
    private static final String ASSIGN_REALM_ROLES_URI = "/admin/realms/{realm}/users/{userId}/role-mappings/realm";

    private final WebClient webClient;
    private final KeycloakAdminTokenClient tokenClient;
    private final KeycloakAdminProperties keycloakProps;
    private final IdentityProvisioningProperties identityProps;

    public KeycloakIdentityAdapter(
            WebClient.Builder builder,
            KeycloakAdminTokenClient tokenClient,
            KeycloakAdminProperties keycloakProps,
            IdentityProvisioningProperties identityProps
    ) {
        this.tokenClient = tokenClient;
        this.keycloakProps = keycloakProps;
        this.identityProps = identityProps;
        this.webClient = builder.baseUrl(keycloakProps.baseUrl()).build();
    }

    /**
     * Compatibilidad con el port actual:
     * - Por defecto creamos STUDENT
     * Nota: En tu realm los roles existen como ROLE_*
     */
    @Override
    public Mono<String> createIdentity(String email) {
        return createIdentity(email, List.of("ROLE_STUDENT"));
    }

    /**
     * Crea usuario:
     * 1) POST user
     * 2) PUT reset-password (temporal)
     * 3) POST assign realm roles
     */
    public Mono<String> createIdentity(String email, List<String> realmRoles) {
        var request = buildCreateUserRequest(email);
        var tempPassword = identityProps.defaultTemporaryPassword(); // ej: "password"

        var roles = (realmRoles == null || realmRoles.isEmpty())
                ? List.of("ROLE_STUDENT")
                : realmRoles;

        return tokenClient.getAccessToken()
                .flatMap(token ->
                        createUser(token, request)
                                .flatMap(userId ->
                                        setTemporaryPassword(token, userId, tempPassword)
                                                .then(assignRealmRoles(token, userId, roles))
                                                .thenReturn(userId)
                                )
                )
                .doOnSuccess(userId -> log.info("Identity provisioned. email={} userId={} roles={}", email, userId, roles))
                .thenReturn(email)
                .retryWhen(retryPolicyCreate());
    }

    /**
     * POST /users -> extrae userId del Location header.
     */
    private @NonNull Mono<String> createUser(String token, CreateUserRequest request) {
        return webClient.post()
                .uri(CREATE_USER_URI, keycloakProps.realm())
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestWithoutCredentials(request))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().value() == 409) {
                        return Mono.error(new EmailAlreadyExistsException(request.email()));
                    }
                    if (resp.statusCode().is2xxSuccessful()) {
                        var location = resp.headers().asHttpHeaders().getLocation();
                        if (location == null) {
                            return Mono.error(new IllegalStateException("Keycloak did not return Location header after creating user"));
                        }
                        var path = location.getPath(); // .../users/{id}
                        var userId = path.substring(path.lastIndexOf('/') + 1);
                        log.debug("Keycloak user created. email={} userId={}", request.email(), userId);
                        return Mono.just(userId);
                    }
                    return fail(resp, "Create user failed");
                });
    }

    /**
     * PUT reset-password: forma más confiable de setear password por Admin API.
     */
    private @NonNull Mono<Void> setTemporaryPassword(String token, String userId, String password) {
        return webClient.put()
                .uri(RESET_PASSWORD_URI, keycloakProps.realm(), userId)
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ResetPasswordRequest("password", password, true))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful() || resp.statusCode().value() == 204) {
                        log.debug("Temporary password set. userId={}", userId);
                        return Mono.empty();
                    }
                    return fail(resp, "Reset password failed (userId=" + userId + ")");
                });
    }

    private record ResetPasswordRequest(String type, String value, boolean temporary) {}

    private @NonNull Mono<Void> assignRealmRoles(String token, String userId, List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return Mono.empty();

        // 1) Validar que existan (si falla, explota aquí con error claro)
        Mono<Void> validateAll = Flux.fromIterable(roleNames)
                .concatMap(role -> validateRoleExists(token, role)) // concat => más determinístico
                .then();

        // 2) Obtener representaciones (AQUÍ sí necesitamos LISTA, no Void)
        Mono<List<Map<String, Object>>> roleReps = Flux.fromIterable(roleNames)
                .concatMap(role -> getRealmRoleRepresentation(token, role))
                .collectList();

        // 3) Asignar roles
        return validateAll
                .then(roleReps)
                .flatMap(reps -> {
                    if (reps.isEmpty()) return Mono.empty();

                    return webClient.post()
                            .uri(ASSIGN_REALM_ROLES_URI, keycloakProps.realm(), userId)
                            .headers(h -> h.setBearerAuth(token))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(reps)
                            .exchangeToMono(resp -> {
                                // Keycloak suele responder 204
                                if (resp.statusCode().value() == 204 || resp.statusCode().is2xxSuccessful()) {
                                    log.debug("Roles assigned. userId={} roles={}", userId, roleNames);
                                    return Mono.<Void>empty();
                                }
                                return fail(resp, "Assign realm roles failed (userId=" + userId + ")");
                            });
                });
    }

    private @NonNull Mono<Void> validateRoleExists(String token, String roleName) {
        return webClient.get()
                .uri(REALM_ROLE_URI, keycloakProps.realm(), roleName)
                .headers(h -> h.setBearerAuth(token))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().value() == 404) {
                        return Mono.error(new IllegalStateException(
                                "Realm role not found in Keycloak: " + roleName +
                                        " (case-sensitive). Verify it exists in realm: " + keycloakProps.realm()
                        ));
                    }
                    if (resp.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    return fail(resp, "Validate realm role failed (role=" + roleName + ")");
                });
    }

    private @NonNull Mono<Map<String, Object>> getRealmRoleRepresentation(String token, String roleName) {
        return webClient.get()
                .uri(REALM_ROLE_URI, keycloakProps.realm(), roleName)
                .headers(h -> h.setBearerAuth(token))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
                    }
                    return fail(resp, "Get role representation failed (role=" + roleName + ")");
                });
    }

    private @NonNull CreateUserRequest buildCreateUserRequest(String email) {
        return new CreateUserRequest(
                email,
                email,
                true,
                true,
                List.of("UPDATE_PASSWORD") // obliga cambio al primer login
        );
    }

    @Override
    public Mono<Void> disableIdentity(String email) {
        return tokenClient.getAccessToken()
                .flatMap(token -> findUserIdByUsername(token, email)
                        .flatMap(userId -> disableUser(token, userId)))
                .retryWhen(retryPolicyDisable());
    }

    private @NonNull Mono<String> findUserIdByUsername(String token, String username) {
        return webClient.get()
                .uri(SEARCH_BY_USERNAME_URI, keycloakProps.realm(), username)
                .headers(h -> h.setBearerAuth(token))
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(new ParameterizedTypeReference<List<KeycloakUserResponse>>() {})
                                .flatMap(users -> {
                                    if (users.isEmpty()) {
                                        return Mono.error(new UserNotFoundInIdentityProviderException(username));
                                    }
                                    return Mono.just(users.getFirst().id());
                                });
                    }
                    return fail(resp, "Search user failed (username=" + username + ")");
                });
    }

    private @NonNull Mono<Void> disableUser(String token, String userId) {
        UpdateUserRequest request = new UpdateUserRequest(false);

        return webClient.put()
                .uri(UPDATE_USER_URI, keycloakProps.realm(), userId)
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful() || resp.statusCode().value() == 204) {
                        log.debug("User disabled. userId={}", userId);
                        return Mono.empty();
                    }
                    return fail(resp, "Disable user failed (userId=" + userId + ")");
                });
    }

    /**
     * Helper genérico: devuelve Mono<T> que siempre termina en error,
     * pero tipado para que compile en cualquier exchangeToMono.
     */
    private <T> Mono<T> fail(ClientResponse resp, String prefix) {
        return resp.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> {
                    var msg = prefix + ". status=" + resp.statusCode().value() + " body=" + body;
                    log.error(msg);
                    return Mono.error(new IllegalStateException(msg));
                });
    }

    private @NonNull Retry retryPolicyCreate() {
        return Retry.backoff(3, Duration.ofSeconds(1))
                .filter(ex -> {
                    if (ex instanceof EmailAlreadyExistsException) return false;

                    if (ex instanceof WebClientResponseException wex) {
                        // 4xx (except timeout) => config/auth/data error => no retry
                        if (wex.getStatusCode().is4xxClientError()
                                && wex.getStatusCode() != HttpStatus.REQUEST_TIMEOUT) {
                            return false;
                        }
                        return wex.getStatusCode().is5xxServerError()
                                || wex.getStatusCode() == HttpStatus.REQUEST_TIMEOUT;
                    }

                    return true;
                });
    }

    private @NonNull Retry retryPolicyDisable() {
        return Retry.backoff(3, Duration.ofSeconds(1))
                .filter(ex -> !(ex instanceof UserNotFoundInIdentityProviderException));
    }

    private CreateUserRequest requestWithoutCredentials(CreateUserRequest request) {
        // Si tu DTO no tiene credentials, devuelve request tal cual.
        // Si lo tuviera, aquí clona sin credenciales.
        return request;
    }
}
