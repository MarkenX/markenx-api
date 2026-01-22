package com.udla.markenx.api.classroom.assignments.infrastructure.seeders.valueobjects;

import java.time.LocalDateTime;

public record TaskSeedDefinition(
        String title,
        String description,
        LocalDateTime deadline,
        double acceptanceRate,
        int maxAttempts,
        boolean outdated
) {}