package com.udla.markenx.api.users.infrastructure.keycloak.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Configuration
@Profile("dev")
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakDevSecretConfig {

    @PostConstruct
    public void init() {
        log.info("KeycloakDevSecretConfig initialized for DEV profile");
    }

    @Bean
    public String keycloakClientSecret(KeycloakProperties props) {
        try {
            if (props.clientSecretFile() == null) {
                throw new IllegalStateException("keycloak.client-secret-file must be configured");
            }

            var filePath = props.clientSecretFile().toAbsolutePath();
            log.info("Reading client secret from file: {}", filePath);

            return getString(props, filePath, log);

        } catch (IOException e) {
            log.error("Error reading client secret file: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to read Keycloak client secret", e);
        }
    }

    @NonNull
    static String getString(KeycloakProperties props, Path filePath, Logger log) throws IOException {
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

        log.info("Client secret file read successfully (length={})", secret.length());
        return secret;
    }
}