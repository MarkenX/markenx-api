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

        int year = 2025;
        var startDate = LocalDate.of(year, 1, 1);
        var endDate = LocalDate.of(year, 5, 30);

        var query = new SaveAcademicTermCommand(startDate, endDate, year, true);
        try {
            AcademicTerm saved = service.handle(query);
            log.info("The term {} was created", saved.toString());
            log.info("Academic terms seeded successfully.");
        } catch (AcademicTermException e) {
            log.error(e.getMessage(), e);
            log.info("Academic terms seeding failed.");
        }
    }
}
