package com.udla.markenx.api.classroom.users.infrastructure.keycloak.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {}
