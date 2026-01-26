package com.udla.markenx.api.security.infrastructure.gametoken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that authenticates requests using game tokens (JWT Bearer tokens).
 * This allows Unity WebGL to access protected endpoints without session cookies.
 *
 * The filter only processes requests with Authorization: Bearer headers
 * when there is no existing authentication in the security context.
 */
@Slf4j
@RequiredArgsConstructor
public class GameTokenAuthenticationFilter extends OncePerRequestFilter {

    private final GameTokenService gameTokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Only process if:
        // 1. There's a Bearer token
        // 2. No existing authentication
        if (authHeader != null &&
                authHeader.startsWith("Bearer ") &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            try {
                GameTokenService.GameTokenClaims claims = gameTokenService.validateAndGetClaims(token);

                List<SimpleGrantedAuthority> authorities = claims.roles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                claims.username(),
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authenticated user via game token: {}", claims.username());

            } catch (InvalidGameTokenException e) {
                // Token invalid - continue without authentication
                // Spring Security will reject the request if authentication is required
                log.debug("Invalid game token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip filtering for token refresh endpoint to allow expired tokens
        // The refresh endpoint handles its own token validation with grace period
        String path = request.getServletPath();
        return path.equals("/auth/game-token/refresh");
    }
}
