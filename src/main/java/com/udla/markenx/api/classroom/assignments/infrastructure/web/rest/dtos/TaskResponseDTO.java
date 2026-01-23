package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        String id,
        String label,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        int maxAttempts,
        String courseId,
        String scenarioId
) {
}
