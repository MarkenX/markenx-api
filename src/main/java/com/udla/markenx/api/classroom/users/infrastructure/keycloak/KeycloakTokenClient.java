package com.udla.markenx.api.classroom.users.infrastructure.keycloak;

import com.udla.markenx.api.classroom.users.infrastructure.keycloak.configuration.KeycloakProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KeycloakTokenClient {

    private final WebClient webClient;
    private final KeycloakProperties props;
    private final String clientSecret;

    public KeycloakTokenClient(
            WebClient.Builder builder,
            KeycloakProperties props,
            @Qualifier("keycloakClientSecret") String clientSecret
    ) {
        this.props = props;
        this.clientSecret = clientSecret;

        var baseUrl = String.format(
                "%s://%s:%d",
                props.scheme(),
                props.host(),
                props.port()
        );

        log.info("Initializing KeycloakTokenClient for URL: {}", baseUrl);
        log.info("Using realm: {}, clientId: {}, clientSecret: {}", props.realm(), props.clientId(), clientSecret);

        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<String> getAccessToken() {
        log.debug("Requesting access token from Keycloak");

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", props.realm())
                .headers(headers ->
                        headers.setBasicAuth(props.clientId(), clientSecret)
                )
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(response -> log.debug("Successfully obtained access token"))
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException responseError) {
                        log.error("Keycloak token request failed: {} - {}",
                                responseError.getStatusCode(),
                                responseError.getResponseBodyAsString());
                    } else {
                        log.error("Error obtaining access token: {}", error.getMessage(), error);
                    }
                })
                .map(TokenResponse::accessToken);
    }

    record TokenResponse(String access_token) {
        String accessToken() { return access_token; }
    }
}