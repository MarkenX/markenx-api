package com.udla.markenx.api.students.application.infrastructure.seeders;

import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@Order(3)
@RequiredArgsConstructor
public class StudentSeeder implements CommandLineRunner {

    private final Flyway flyway;

    @Override
    public void run(String... args) throws Exception {

    }
}
