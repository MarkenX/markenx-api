package com.udla.markenx.api.security.infrastructure.gametoken;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for game token generation.
 * Used for Unity WebGL authentication via Bearer tokens.
 */
@ConfigurationProperties(prefix = "app.game-token")
public record GameTokenProperties(
        /**
         * Secret key for signing JWT tokens.
         * Must be at least 32 characters for HS256.
         */
        String secret,

        /**
         * Token duration in seconds.
         * Default: 600 (10 minutes)
         */
        long durationSeconds,

        /**
         * Grace period in seconds for token refresh.
         * Allows refreshing tokens that expired within this window.
         * Default: 300 (5 minutes)
         */
        long refreshGracePeriodSeconds
) {
    public GameTokenProperties {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("Game token secret must be at least 32 characters");
        }
        if (durationSeconds <= 0) {
            durationSeconds = 600;
        }
        if (refreshGracePeriodSeconds <= 0) {
            refreshGracePeriodSeconds = 300;
        }
    }
}
