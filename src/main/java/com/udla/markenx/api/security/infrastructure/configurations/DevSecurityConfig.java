package com.udla.markenx.api.security.infrastructure.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.*;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

    /**
     * En DEV trabajamos con SPA(s) en localhost y el BFF en otro puerto.
     * Como usamos cookie de sesión (JSESSIONID), se requiere CORS con credentials.
     */
    private static final List<String> DEV_ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "http://localhost:3001"
    );

    /**
     * Endpoint público del BFF para iniciar login desde el frontend.
     * El frontend jamás llama a Keycloak directo.
     */
    private static final String BFF_LOGIN_ENDPOINT = "/auth/login";

    /**
     * Endpoint de logout “lógico” del BFF.
     * Spring Security lo intercepta con LogoutFilter.
     */
    private static final String BFF_LOGOUT_ENDPOINT = "/auth/logout";

    /**
     * “Bridge” post logout:
     * Keycloak redirige al BFF, y el BFF redirige al frontend.
     * Esto evita que Keycloak deba “conocer” URLs del SPA (más seguro y más portable por ambientes).
     */
    private static final String BFF_POST_LOGOUT_ENDPOINT = "/auth/post-logout";

    /**
     * Frontend principal en DEV (fallback seguro).
     */
    private static final String DEV_DEFAULT_FRONTEND = "http://localhost:3000/";

    /**
     * Ruta pública del frontend usada como “pantalla de salida”.
     */
    private static final String DEV_LOGGED_OUT_PAGE = "http://localhost:3000/logged-out";

    @Bean
    SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http,
                                            @NonNull ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        http
                /**
                 * CORS requerido porque:
                 * - el BFF está en 8080 y el SPA en 3000/3001 (orígenes distintos)
                 * - usamos cookies (credentials: include)
                 */
                .cors(Customizer.withDefaults())

                /**
                 * DEV: deshabilitado por simplicidad (solo DEV).
                 * En prod se recomienda habilitar CSRF si mantienes cookies y endpoints mutables.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Herramientas dev
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Contrato BFF para auth
                        .requestMatchers("/auth/**").permitAll()

                        // El resto protegido
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        /**
                         * Mapeo de authorities:
                         * - preserva scopes
                         * - añade realm roles ROLE_*
                         */
                        .userInfoEndpoint(u -> u.oidcUserService(oidcUserService()))
                        /**
                         * Redirect controlado para SPA:
                         * - evita caer en "/" del BFF después del login
                         */
                        .successHandler(frontendRedirectSuccessHandler())
                )

                .logout(l -> l
                        /**
                         * /auth/logout es el endpoint “contrato” para el frontend.
                         * Importante: Spring intercepta antes de controller.
                         */
                        .logoutUrl(BFF_LOGOUT_ENDPOINT)

                        // Limpieza local del BFF
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)

                        /**
                         * Logout federado (Keycloak):
                         * - genera logout URL con id_token_hint
                         * - redirige de vuelta al BFF (bridge)
                         */
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
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
