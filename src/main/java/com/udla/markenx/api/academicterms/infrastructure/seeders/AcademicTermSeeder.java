package com.udla.markenx.api.academicterms.infrastructure.seeders;

import com.udla.markenx.api.academicterms.application.queries.SaveAcademicTermQuery;
import com.udla.markenx.api.academicterms.application.services.AcademicTermQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class AcademicTermSeeder implements CommandLineRunner {

    private final AcademicTermQueryService service;

    @Override
    public void run(String @NotNull ... args) throws Exception {
        log.info("Seeding academic terms...");

        int year = 2025;
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 5, 30);

        log.info("Creating academic term {}...", year);

        SaveAcademicTermQuery query = new SaveAcademicTermQuery(startDate, endDate, year, true);
        service.save(query);
        
        log.info("Academic terms seeded successfully.");
    }
}
