package com.udla.markenx.api.classroom.users.infrastructure.keycloak.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Configuration
@Profile("prod")
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakProdSecretConfig {

    @PostConstruct
    public void init() {
        log.info("KeycloakProdSecretConfig initialized for PROD profile");
    }

    @Bean
    public String keycloakClientSecret(KeycloakProperties props) {
        try {
            if (props.clientSecretFile() == null) {
                throw new IllegalStateException(
                        "keycloak.client-secret-file must be configured for production"
                );
            }

            var filePath = props.clientSecretFile().toAbsolutePath();
            log.info("Reading client secret from mounted file: {}", filePath);

            if (!Files.exists(props.clientSecretFile())) {
                log.error("Client secret file does NOT exist: {}", filePath);
                throw new IllegalStateException(
                        String.format("Keycloak client secret file not found: %s", filePath)
                );
            }

            var secret = Files.readString(props.clientSecretFile()).trim();

            if (secret.isBlank()) {
                log.error("Client secret file is EMPTY: {}", filePath);
                throw new IllegalStateException("Keycloak client secret file is empty");
            }

            log.info("Client secret loaded successfully from mounted secret");
            return secret;

        } catch (IOException e) {
            log.error("Error reading client secret file: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to read Keycloak client secret", e);
        }
    }
}
