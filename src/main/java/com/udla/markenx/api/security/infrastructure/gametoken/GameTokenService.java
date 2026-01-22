package com.udla.markenx.api.security.infrastructure.gametoken;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service for generating and validating game tokens (JWT).
 * Used for Unity WebGL authentication via Bearer tokens.
 */
@Slf4j
@Service
public class GameTokenService {

    private final GameTokenProperties properties;
    private final JWSSigner signer;
    private final JWSVerifier verifier;

    public GameTokenService(GameTokenProperties properties) {
        this.properties = properties;
        try {
            byte[] secretBytes = properties.secret().getBytes();
            this.signer = new MACSigner(secretBytes);
            this.verifier = new MACVerifier(secretBytes);
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to initialize JWT signer/verifier", e);
        }
    }

    /**
     * Generates a game token for the authenticated OIDC user.
     *
     * @param user       The authenticated OIDC user
     * @param authorities The user's granted authorities
     * @return GameTokenResponse with the token and expiration info
     */
    public GameTokenResponse generateToken(OidcUser user, Collection<? extends GrantedAuthority> authorities) {
        String username = user.getPreferredUsername();
        String email = user.getEmail();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a != null && a.startsWith("ROLE_"))
                .toList();

        return generateTokenInternal(username, email, roles);
    }

    /**
     * Generates a game token from claims extracted from a previous token.
     *
     * @param username The username
     * @param email    The email
     * @param roles    The roles
     * @return GameTokenResponse with the token and expiration info
     */
    public GameTokenResponse generateToken(String username, String email, List<String> roles) {
        return generateTokenInternal(username, email, roles);
    }

    private GameTokenResponse generateTokenInternal(String username, String email, List<String> roles) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(properties.durationSeconds());

        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .claim("email", email)
                    .claim("roles", roles)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiration))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );
            signedJWT.sign(signer);

            String token = signedJWT.serialize();
            log.debug("Generated game token for user: {}", username);

            return new GameTokenResponse(token, properties.durationSeconds());
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to sign JWT token", e);
        }
    }

    /**
     * Refreshes an existing token.
     * Allows refresh even if the token has expired within the grace period.
     *
     * @param currentToken The current (possibly expired) token
     * @return New GameTokenResponse with fresh token
     * @throws InvalidGameTokenException if the token is invalid or expired beyond grace period
     */
    public GameTokenResponse refreshToken(String currentToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(currentToken);

            if (!signedJWT.verify(verifier)) {
                throw new InvalidGameTokenException("Invalid token signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expiration = claims.getExpirationTime();

            // Check if token has expired beyond the grace period
            long expiredAgoMs = System.currentTimeMillis() - expiration.getTime();
            long gracePeriodMs = properties.refreshGracePeriodSeconds() * 1000;

            if (expiredAgoMs > gracePeriodMs) {
                throw new InvalidGameTokenException(
                        "Token expired more than " + properties.refreshGracePeriodSeconds() + " seconds ago"
                );
            }

            // Extract claims and generate new token
            String username = claims.getSubject();
            String email = (String) claims.getClaim("email");
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");

            log.debug("Refreshing game token for user: {}", username);
            return generateToken(username, email, roles != null ? roles : List.of());

        } catch (ParseException e) {
            throw new InvalidGameTokenException("Failed to parse token", e);
        } catch (JOSEException e) {
            throw new InvalidGameTokenException("Failed to verify token", e);
        }
    }

    /**
     * Validates a token and returns its claims.
     *
     * @param token The JWT token to validate
     * @return The token claims
     * @throws InvalidGameTokenException if the token is invalid or expired
     */
    public GameTokenClaims validateAndGetClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(verifier)) {
                throw new InvalidGameTokenException("Invalid token signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Check expiration
            Date expiration = claims.getExpirationTime();
            if (expiration != null && expiration.before(new Date())) {
                throw new InvalidGameTokenException("Token has expired");
            }

            String username = claims.getSubject();
            String email = (String) claims.getClaim("email");
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getClaim("roles");

            return new GameTokenClaims(
                    username,
                    email,
                    roles != null ? roles : List.of()
            );

        } catch (ParseException e) {
            throw new InvalidGameTokenException("Failed to parse token", e);
        } catch (JOSEException e) {
            throw new InvalidGameTokenException("Failed to verify token", e);
        }
    }

    /**
     * Claims extracted from a validated game token.
     */
    public record GameTokenClaims(
            String username,
            String email,
            List<String> roles
    ) {}
}
