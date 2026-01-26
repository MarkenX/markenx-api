package com.udla.markenx.api.security.infrastructure.keycloak.configurations;

import com.udla.markenx.api.security.infrastructure.keycloak.properties.KeycloakAdminProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakAdminProperties.class)
public class KeycloakAdminConfig {}
