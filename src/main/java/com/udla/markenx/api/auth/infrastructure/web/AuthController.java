package com.udla.markenx.api.auth.infrastructure.web;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Inicia el login redirigiendo al entrypoint estándar de Spring Security.
     * Esto mantiene el frontend desacoplado de Keycloak/OAuth2.
     */
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws Exception {
        response.sendRedirect("/oauth2/authorization/keycloak");
    }

    /**
     * Devuelve información mínima del usuario autenticado para el frontend.
     * Si no está autenticado, Spring Security devolverá 401 en endpoints protegidos.
     */
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Map.of("authenticated", false);
        }

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String username = authentication.getName();

        // Si es OAuth2, puedes extraer claims adicionales:
        if (authentication instanceof OAuth2AuthenticationToken oauth2) {
            Object email = oauth2.getPrincipal().getAttributes().get("email");
            if (email != null) {
                return Map.of(
                        "authenticated", true,
                        "username", username,
                        "email", email,
                        "roles", roles
                );
            }
        }

        return Map.of(
                "authenticated", true,
                "username", username,
                "roles", roles
        );
    }
}
