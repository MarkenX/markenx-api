package com.udla.markenx.api.classroom.academicterms.infrastructure.seeders;

import com.udla.markenx.api.classroom.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.incoming.SaveAcademicTermUseCase;
import com.udla.markenx.api.classroom.academicterms.domain.exceptions.AcademicTermException;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class AcademicTermSeeder implements CommandLineRunner {

    private final SaveAcademicTermUseCase service;
    private final Flyway flyway;

    @Override
    public void run(String @NotNull ... args) {
        log.info("Seeding academic terms...");

        try {
            // Periodo académico activo: Feb 2026 - Jul 2026
            var activeTerm = new SaveAcademicTermCommand(
                    LocalDate.of(2026, 2, 1),
                    LocalDate.of(2026, 6, 1),
                    2025,
                    true
            );
            AcademicTerm savedActive = service.handle(activeTerm);
            log.info("Active term created: {}", savedActive.getId());

            log.info("Academic terms seeded successfully.");
        } catch (AcademicTermException e) {
            log.error(e.getMessage(), e);
            log.info("Academic terms seeding failed.");
        }
    }
}
