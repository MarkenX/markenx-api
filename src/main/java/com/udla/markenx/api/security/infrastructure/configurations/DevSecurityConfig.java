package com.udla.markenx.api.security.infrastructure.configurations;

import com.udla.markenx.api.security.infrastructure.gametoken.GameTokenAuthenticationFilter;
import com.udla.markenx.api.security.infrastructure.gametoken.GameTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevSecurityConfig {

    private final GameTokenService gameTokenService;

    /**
     * En DEV trabajamos con SPA(s) en localhost y el BFF en otro puerto.
     * Como usamos cookie de sesión (JSESSIONID), se requiere CORS con credentials.
     */
    private static final List<String> DEV_ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "http://localhost:3001"
    );

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
                         * /auth/logout es el endpoint "contrato" para el frontend.
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
                )

                /**
                 * Game token filter:
                 * Allows Unity WebGL to authenticate via Bearer token
                 * when session cookies are not available.
                 */
                .addFilterBefore(
                        new GameTokenAuthenticationFilter(gameTokenService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * Logout federado OIDC:
     * - Keycloak puede rechazar post_logout_redirect_uri si no está “whitelisted”.
     * - Por eso usamos un bridge en el BFF: {baseUrl}/auth/post-logout
     * - Luego el BFF redirige al frontend.
     */
    @Bean
    LogoutSuccessHandler oidcLogoutSuccessHandler(@NonNull ClientRegistrationRepository clients) {
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(clients);

        // Usa el host/puerto real con el que se accede al BFF (incluye context-path).
        // En tu caso: http://localhost:8080/api/v1/auth/post-logout
        handler.setPostLogoutRedirectUri("{baseUrl}" + BFF_POST_LOGOUT_ENDPOINT);

        return handler;
    }

    /**
     * CORS:
     * allowCredentials=true es obligatorio para que el browser envíe/reciba JSESSIONID.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(DEV_ALLOWED_ORIGINS);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        /**
         * Si tu API expone paginación por header (React Admin / DataGrid),
         * aquí debes exponer los headers a JS.
         */
        config.setExposedHeaders(List.of("X-Total-Count"));

        // Permite cookies cross-origin (solo con orígenes explícitos, no con "*")
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * UserService OIDC:
     * - añade realm roles desde claim realm_access.roles
     * - normaliza a prefijo ROLE_
     * - preserva authorities existentes (OIDC_USER, scopes, etc.)
     */
    @Bean
    OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            Set<SimpleGrantedAuthority> mapped = new HashSet<>();

            // 1) Preservar authorities existentes
            for (GrantedAuthority a : oidcUser.getAuthorities()) {
                String authority = a.getAuthority();
                if (authority != null && !authority.isBlank()) {
                    mapped.add(new SimpleGrantedAuthority(authority));
                }
            }

            // 2) Agregar realm roles como ROLE_*
            for (String role : extractRealmRoles(oidcUser)) {
                String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                mapped.add(new SimpleGrantedAuthority(normalized));
            }

            // Mantener token + claims
            OidcIdToken idToken = oidcUser.getIdToken();
            OidcUserInfo userInfo = oidcUser.getUserInfo();

            // preferred_username suele ser la mejor clave para “name” en UI/logs
            return new DefaultOidcUser(mapped, idToken, userInfo, "preferred_username");
        };
    }

    private List<String> extractRealmRoles(@NonNull OidcUser user) {
        Object realmAccess = user.getClaims().get("realm_access");
        if (!(realmAccess instanceof Map<?, ?> map)) return List.of();

        Object roles = map.get("roles");
        if (!(roles instanceof Collection<?> c)) return List.of();

        return c.stream().map(Object::toString).toList();
    }

    /**
     * SuccessHandler:
     * Orden recomendado:
     * 1) redirect explícito y validado (?redirect=...)
     * 2) SavedRequest (si Spring guardó una URL original)
     * 3) fallback al frontend default
     */
    @Bean
    AuthenticationSuccessHandler frontendRedirectSuccessHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) -> {

            String redirect = request.getParameter("redirect");
            if (redirect != null && isAllowedRedirect(redirect)) {
                response.sendRedirect(redirect);
                return;
            }

            SavedRequest saved = new HttpSessionRequestCache().getRequest(request, response);
            if (saved != null && isAllowedRedirect(saved.getRedirectUrl())) {
                response.sendRedirect(saved.getRedirectUrl());
                return;
            }

            response.sendRedirect(DEV_DEFAULT_FRONTEND);
        };
    }

    /**
     * Whitelist de redirects en DEV:
     * Evita open redirect vulnerabilities desde parámetros manipulables.
     */
    private boolean isAllowedRedirect(@NonNull String url) {
        return url.startsWith("http://localhost:3000/")
                || url.startsWith("http://localhost:3001/");
    }
}
