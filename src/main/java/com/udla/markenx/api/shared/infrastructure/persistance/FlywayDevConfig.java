package com.udla.markenx.api.shared.infrastructure.persistance;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Configuration
@Profile("dev")
public class FlywayDevConfig {

    @Bean
    @Order(0)
    CommandLineRunner resetDatabase(Flyway flyway) {
        return args -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
