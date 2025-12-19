package com.udla.markenx.api.courses.infrastructure.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class CourseSeeder implements CommandLineRunner {

    @Override
    public void run(String @NonNull ... args) throws Exception {
        log.info("Seeding courses...");
    }
}