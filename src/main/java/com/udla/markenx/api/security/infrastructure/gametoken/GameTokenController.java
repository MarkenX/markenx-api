package com.udla.markenx.api.security.infrastructure.gametoken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for game token management.
 * Provides endpoints for Unity WebGL to obtain and refresh authentication tokens.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GameTokenController {

    private final GameTokenService gameTokenService;

    /**
     * Generates a temporary JWT token for Unity WebGL game authentication.
     * Requires an active session (cookie JSESSIONID) from the BFF.
     *
     * @param authentication The current authentication (from OAuth2 session)
     * @return GameTokenResponse with the token and expiration info
     */
    @PostMapping("/game-token")
    public ResponseEntity<GameTokenResponse> generateGameToken(Authentication authentication) {
        if (!isValidOAuth2Authentication(authentication)) {
            log.warn("Game token request without valid OAuth2 authentication");
            return ResponseEntity.status(401).build();
        }

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OidcUser user = (OidcUser) oauthToken.getPrincipal();

        GameTokenResponse response = gameTokenService.generateToken(user, oauthToken.getAuthorities());
        log.info("Generated game token for user: {}", user.getPreferredUsername());

        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes an existing game token before it expires.
     * Allows extending the session if the user is still playing.
     * The current token can be close to expiring or recently expired (within grace period).
     *
     * @param authHeader The Authorization header with Bearer token
     * @return New GameTokenResponse with fresh token
     */
    @PostMapping("/game-token/refresh")
    public ResponseEntity<GameTokenResponse> refreshGameToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Game token refresh request without Bearer token");
            return ResponseEntity.status(401).build();
        }

        String currentToken = authHeader.substring(7);

        try {
            GameTokenResponse response = gameTokenService.refreshToken(currentToken);
            log.debug("Refreshed game token successfully");
            return ResponseEntity.ok(response);
        } catch (InvalidGameTokenException e) {
            log.warn("Failed to refresh game token: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    private boolean isValidOAuth2Authentication(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            return false;
        }
        if (!authentication.isAuthenticated()) {
            return false;
        }
        return oauthToken.getPrincipal() instanceof OidcUser;
    }
}
