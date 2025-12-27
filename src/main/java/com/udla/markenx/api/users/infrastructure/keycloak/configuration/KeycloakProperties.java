package com.udla.markenx.api.users.infrastructure.keycloak.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String scheme,
        String host,
        int port,
        String realm,
        String clientId,
        String clientSecret
) {}
