package com.udla.markenx.api.security.infrastructure.keycloak.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "identity")
public record IdentityProvisioningProperties(
        String defaultTemporaryPassword
) {}