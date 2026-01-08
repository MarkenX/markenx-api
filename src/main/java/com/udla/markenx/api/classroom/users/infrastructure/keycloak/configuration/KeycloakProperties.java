package com.udla.markenx.api.classroom.users.infrastructure.keycloak.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String scheme,
        String host,
        int port,
        String realm,
        String clientId,
        String clientSecret,
        Path clientSecretFile
) {}
