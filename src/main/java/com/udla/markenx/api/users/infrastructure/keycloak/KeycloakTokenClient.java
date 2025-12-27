package com.udla.markenx.api.users.infrastructure.keycloak;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KeycloakTokenClient {

    private final WebClient webClient;
    private final KeycloakProperties props;

    KeycloakTokenClient(WebClient.Builder builder, KeycloakProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.baseUrl()).build();
    }

    public Mono<String> getAccessToken() {
        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", props.realm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", props.clientId())
                        .with("client_secret", props.clientSecret()))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::accessToken);
    }

    record TokenResponse(String access_token) {
        String accessToken() { return access_token; }
    }
}
