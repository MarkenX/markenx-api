package com.udla.markenx.api.security.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * BFF Auth endpoints for React Admin (Option A):
 * - Frontend does NOT receive/handle access tokens.
 * - Frontend relies on HttpSession cookie issued by the BFF after oauth2Login.
 * <p>
 * Contract:
 * - GET  /auth/login  -> redirects to Spring Security authorization endpoint (/oauth2/authorization/keycloak)
 * - GET  /auth/me     -> 200 with user identity + roles, or 401 if not authenticated
 * - POST /auth/logout -> logout handled by Spring Security filter (configured in SecurityConfig)
 * <p>
 * Why this works with React Admin:
 * - checkAuth(): call /auth/me -> if 200, user is authenticated; if 401, redirect to /auth/login
 * - getPermissions(): you can reuse roles returned by /auth/me
 */
@Slf4j
@RestController
public class AuthController {

    /**
     * Starts the OAuth2 Authorization Code flow via Spring Security.
     * We redirect to the default endpoint created by Spring:
     *   /oauth2/authorization/{registrationId}
     * <p>
     * registrationId MUST match your YAML:
     * spring.security.oauth2.client.registration.keycloak
     */
    @GetMapping("/auth/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/oauth2/authorization/keycloak");
    }

    /**
     * Returns identity and roles for the currently authenticated user.
     * - 200: when session is valid (authenticated)
     * - 401: when no session / not authenticated
     */
    @GetMapping("/auth/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken) || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var principal = oauthToken.getPrincipal();

        String username = attr(principal, "preferred_username");
        String email = null;
        String fullName = attr(principal, "name");

        if (principal instanceof OidcUser oidc) {
            email = oidc.getEmail();
            if (fullName == null) fullName = oidc.getFullName();

            log.debug("OIDC claims keys: {}", oidc.getClaims().keySet());
            log.debug("realm_access: {}", oidc.getClaims().get("realm_access"));
            log.debug("resource_access: {}", oidc.getClaims().get("resource_access"));
        } else {
            email = attr(principal, "email");
        }

        List<String> roles = oauthToken.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(a -> a.startsWith("ROLE_"))
                .distinct()
                .sorted()
                .toList();

        return ResponseEntity.ok(new MeResponse(username, email, fullName, roles));
    }

    /**
     * Bridge post logout:
     * Keycloak redirige aquí luego del logout OIDC.
     * Este endpoint NO valida sesión; su responsabilidad es regresar al SPA a una ruta pública.
     */
    @GetMapping("/auth/post-logout")
    public void postLogout(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/logged-out");
    }

    private String attr(Object principal, String name) {
        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User u) {
            Object v = u.getAttributes().get(name);
            return v == null ? null : v.toString();
        }
        return null;
    }

    public record MeResponse(
            String username,
            String email,
            String fullName,
            List<String> roles
    ) {}
}
