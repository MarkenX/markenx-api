package com.udla.markenx.api.security.infrastructure.configurations;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

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
                .csrf(AbstractHttpConfigurer::disable)

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

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(u -> u.oidcUserService(oidcUserService()))
                )

                /*
                 * Logout local del BFF (invalida sesión).
                 * Luego podemos implementar logout federado hacia Keycloak.
                 */
                .logout(l -> l
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(204))
                );

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
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("X-Total-Count"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            Set<SimpleGrantedAuthority> mappedAuthorities = new HashSet<>();

            // 1) Mantener authorities existentes (scopes, etc.)
            oidcUser.getAuthorities().forEach(a -> mappedAuthorities.add(new SimpleGrantedAuthority(a.getAuthority())));

            // 2) Agregar realm roles como ROLE_*
            extractRealmRoles(oidcUser).forEach(role -> {
                String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                mappedAuthorities.add(new SimpleGrantedAuthority(normalized));
            });

            // Mantener token + claims
            OidcIdToken idToken = oidcUser.getIdToken();
            OidcUserInfo userInfo = oidcUser.getUserInfo();

            // preferred_username como nameAttributeKey suele ser útil
            return new DefaultOidcUser(mappedAuthorities, idToken, userInfo, "preferred_username");
        };
    }

    private List<String> extractRealmRoles(@NonNull OidcUser user) {
        Object realmAccess = user.getClaims().get("realm_access");
        if (!(realmAccess instanceof Map<?, ?> map)) return List.of();

        Object roles = map.get("roles");
        if (!(roles instanceof Collection<?> c)) return List.of();

        return c.stream().map(Object::toString).toList();
    }

}
