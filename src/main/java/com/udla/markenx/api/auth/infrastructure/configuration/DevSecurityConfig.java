package com.udla.markenx.api.auth.infrastructure.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

    /**
     * DEV (BFF):
     * - OAuth2 Login con Keycloak (Authorization Code Flow)
     * - Sesión HttpSession (cookie HTTP-only)
     * - Frontend NO envía JWT, solo cookies
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // React Admin corre en 3000/3001 y el BFF en 8082 -> CORS requerido
                .cors(Customizer.withDefaults())

                /*
                 * CSRF:
                 * - En un BFF con cookies, CSRF es relevante.
                 * - Para avanzar rápido en DEV, ignoramos CSRF solo en /auth/**
                 *   y mantenemos el resto protegido.
                 * <p>
                 * Hardening posterior: CookieCsrfTokenRepository + token en React Admin.
                 */
                .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**"))

                /*
                 * Autorización:
                 * - Swagger permitido en DEV
                 * - /auth/** permitido (login + me)
                 * - Lo demás requiere autenticación
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                // OAuth2 Login (Keycloak). Spring maneja redirects, callback y sesión.
                .oauth2Login(Customizer.withDefaults())

                /*
                 * Logout local del BFF (invalida sesión).
                 * Luego podemos implementar logout federado hacia Keycloak.
                 */
                .logout(logout -> logout.logoutUrl("/auth/logout"));

        return http.build();
    }

    /**
     * CORS:
     * - allowCredentials=true es obligatorio para enviar cookies.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
