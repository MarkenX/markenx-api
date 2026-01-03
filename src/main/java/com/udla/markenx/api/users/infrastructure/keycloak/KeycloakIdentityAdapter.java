package com.udla.markenx.api.users.infrastructure.keycloak;

import com.udla.markenx.api.users.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.users.domain.exceptions.EmailAlreadyExistsException;
import com.udla.markenx.api.users.infrastructure.keycloak.configuration.KeycloakProperties;
import com.udla.markenx.api.users.infrastructure.keycloak.dtos.CreateUserRequest;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class KeycloakIdentityAdapter implements ExternalIdentityPort {

    private static final String CREATE_USER_URI = "/admin/realms/{realm}/users";

    private final WebClient webClient;
    private final KeycloakTokenClient tokenClient;
    private final KeycloakProperties props;

    public KeycloakIdentityAdapter(WebClient.Builder builder,
                                   KeycloakTokenClient tokenClient,
                                   KeycloakProperties props) {
        this.tokenClient = tokenClient;
        this.props = props;
        this.webClient = builder
                .baseUrl(buildBaseUrl(props))
                .build();
    }

    @Override
    public Mono<String> createIdentity(String email) {
        CreateUserRequest request = buildCreateUserRequest(email);

        return tokenClient.getAccessToken()
                .flatMap(token -> createUser(token, request))
                .thenReturn(email)
                .retryWhen(retryPolicy());
    }

    private @NonNull Mono<Void> createUser(String token, CreateUserRequest request) {
        return webClient.post()
                .uri(CREATE_USER_URI, props.realm())
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.value() == 409,
                        resp -> Mono.error(
                                new EmailAlreadyExistsException(request.email())
                        )
                )
                .bodyToMono(Void.class);
    }

    @Contract(" -> new")
    private @NonNull Retry retryPolicy() {
        return Retry.backoff(3, Duration.ofSeconds(1))
                .filter(ex -> !(ex instanceof EmailAlreadyExistsException));
    }

    @Contract("_ -> new")
    private @NonNull CreateUserRequest buildCreateUserRequest(String email) {
        return new CreateUserRequest(
                email,
                email,
                true,
                true,
                List.of("UPDATE_PASSWORD")
        );
    }

    private static @NonNull String buildBaseUrl(@NonNull KeycloakProperties props) {
        return String.format(
                "%s://%s:%d",
                props.scheme(),
                props.host(),
                props.port()
        );
    }
}
