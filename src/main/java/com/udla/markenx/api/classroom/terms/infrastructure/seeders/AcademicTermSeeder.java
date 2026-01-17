package com.udla.markenx.api.classroom.terms.infrastructure.seeders;

import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.terms.domain.exceptions.AcademicTermException;
import com.udla.markenx.api.classroom.terms.infrastructure.seeders.factories.TermSeedFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class AcademicTermSeeder implements CommandLineRunner {

    private final CreateTermUseCase service;

    @Override
    public void run(String @NotNull ... args) {
        final long startMs = System.currentTimeMillis();

        log.info("Seeding academic terms...");

        try {
            LocalDate today = LocalDate.now();

            var commands = List.of(
                    TermSeedFactory.past(today),
                    TermSeedFactory.current(today),
                    TermSeedFactory.future(today)
            );

            log.debug("Creating {} academic terms...", commands.size());

            var created = commands.stream()
                    .map(service::handle)
                    .toList();

            created.forEach(term -> log.debug("Created {}", term));

            long tookMs = System.currentTimeMillis() - startMs;
            log.info("Seed completed. created={}, tookMs={}", created.size(), tookMs);

        } catch (AcademicTermException e) {
            long tookMs = System.currentTimeMillis() - startMs;
            log.error("Seed failed. tookMs={}, reason={}", tookMs, e.getMessage(), e);
        }
    }
}
