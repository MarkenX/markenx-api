package com.udla.markenx.api.classroom.users.infrastructure.keycloak.dtos;

public record KeycloakUserResponse(
        String id,
        String username,
        String email,
        boolean enabled
) {
}
