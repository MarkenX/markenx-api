package com.udla.markenx.api.users.infrastructure.keycloak;

import com.udla.markenx.api.users.infrastructure.keycloak.configuration.KeycloakProperties;
import com.udla.markenx.api.users.infrastructure.keycloak.dtos.CreateUserRequest;
import com.udla.markenx.api.users.domain.ports.outgoing.UserIdentityProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
class KeycloakUserIdentityProvider implements UserIdentityProvider {

    private final WebClient webClient;
    private final KeycloakTokenClient tokenClient;
    private final KeycloakProperties props;

    KeycloakUserIdentityProvider(WebClient.Builder builder,
                                 KeycloakTokenClient tokenClient,
                                 KeycloakProperties props) {
        this.tokenClient = tokenClient;
        this.props = props;
        this.webClient = builder.baseUrl(props.baseUrl()).build();
    }

    @Override
    public void provisionIdentity(String email) {

        CreateUserRequest request = new CreateUserRequest(
            email, email, true, true,
            List.of("UPDATE_PASSWORD")
        );

        tokenClient.getAccessToken()
            .flatMap(token ->
                // Posts user creation request; extracts location on success
                webClient.post()
                    .uri("/admin/realms/{realm}/users", props.realm())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchangeToMono(resp -> {
                        // Extracts location header on success
                        if (resp.statusCode().is2xxSuccessful()) {
                            return Mono.just(
                                Objects.requireNonNull(resp.headers().asHttpHeaders()
                                        .getFirst(HttpHeaders.LOCATION))
                            );
                        }
                        return resp.createException().flatMap(Mono::error);
                        })
                ).block();
    }
}
