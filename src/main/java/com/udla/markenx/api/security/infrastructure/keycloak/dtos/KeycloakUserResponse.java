package com.udla.markenx.api.security.infrastructure.keycloak.dtos;

public record KeycloakUserResponse(
        String id,
        String username,
        String email,
        boolean enabled
) {
}
