package com.udla.markenx.api.assignments.infrastructure.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@Order(4)
@RequiredArgsConstructor
public class TaskSeeder implements CommandLineRunner {
    @Override
    public void run(String @NonNull ... args) {

    }
}
