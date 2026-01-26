package com.udla.markenx.api.security.infrastructure.keycloak;

import com.udla.markenx.api.security.infrastructure.keycloak.properties.KeycloakAdminProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeycloakAdminTokenClient {

    private final WebClient webClient;
    private final KeycloakAdminProperties props;

    public KeycloakAdminTokenClient(WebClient.Builder builder, KeycloakAdminProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.baseUrl()).build();
        log.info("KeycloakAdminTokenClient initialized for baseUrl={} authRealm={}", props.baseUrl(), props.realm());
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