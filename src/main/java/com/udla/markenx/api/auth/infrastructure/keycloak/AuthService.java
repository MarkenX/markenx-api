package com.udla.markenx.api.auth.infrastructure.keycloak;

import com.udla.markenx.api.auth.infrastructure.web.dtos.LoginRequestDTO;
import com.udla.markenx.api.auth.infrastructure.web.dtos.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient webClient;

    // Se inyecta valores desde application.properties
    @Value("${keycloak.token-uri:http://localhost:9090/realms/markenx/protocol/openid-connect/token}")
    private String keycloakTokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;


    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<LoginResponseDTO> login(LoginRequestDTO request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("username", request.email());
        formData.add("password", request.password());
        formData.add("grant_type", "password");
        
        // Si el cliente en Keycloak es "Confidential", se descomenta y se usa el secret:
        // formData.add("client_secret", clientSecret); 

        return webClient.post()
                .uri(keycloakTokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LoginResponseDTO.class);
    }
}