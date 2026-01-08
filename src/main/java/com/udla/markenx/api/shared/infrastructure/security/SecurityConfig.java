package com.udla.markenx.api.shared.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    // Rutas públicas
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/onboarding/**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth ->
                    oauth.jwt(Customizer.withDefaults())
            );

        return http.build();
    }
}
