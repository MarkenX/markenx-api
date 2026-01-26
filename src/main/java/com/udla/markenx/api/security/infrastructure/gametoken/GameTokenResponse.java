package com.udla.markenx.api.security.infrastructure.gametoken;

/**
 * Response DTO for game token endpoints.
 *
 * @param token     The JWT token string
 * @param expiresIn Token validity duration in seconds
 */
public record GameTokenResponse(
        String token,
        long expiresIn
) {}
