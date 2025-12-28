package com.udla.markenx.api.users.infrastructure.keycloak.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("test")
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakTestSecretConfig {

    @PostConstruct
    public void init() {
        log.info("KeycloakTestSecretConfig initialized");
    }

    @Bean
    public String keycloakClientSecret(KeycloakProperties props) {
        log.info("Reading client secret from properties");

        if (props.clientSecret() == null || props.clientSecret().isBlank()) {
            throw new IllegalStateException("keycloak.client-secret must be configured in properties");
        }

        log.info("Using client secret from properties (length={})",
                props.clientSecret().length());

        if (props.clientSecret().length() > 10) {
            log.debug("Client secret starts with: {}...",
                    props.clientSecret().substring(0, 5));
        }

        return props.clientSecret();
    }
}