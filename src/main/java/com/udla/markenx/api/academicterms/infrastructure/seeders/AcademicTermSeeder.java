package com.udla.markenx.api.academicterms.infrastructure.seeders;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class AcademicTermSeeder implements CommandLineRunner {

    @Override
    public void run(String @NotNull ... args) throws Exception {

    }
}
