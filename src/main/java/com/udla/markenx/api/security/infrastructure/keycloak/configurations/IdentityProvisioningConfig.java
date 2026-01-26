package com.udla.markenx.api.security.infrastructure.keycloak.configurations;

import com.udla.markenx.api.security.infrastructure.keycloak.properties.IdentityProvisioningProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IdentityProvisioningProperties.class)
public class IdentityProvisioningConfig {}