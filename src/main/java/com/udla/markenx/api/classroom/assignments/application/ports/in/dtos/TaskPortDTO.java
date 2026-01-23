package com.udla.markenx.api.classroom.assignments.application.ports.in.dtos;

import java.time.LocalDateTime;

/**
 * DTO for Task data at the application port layer.
 * Note: status and currentAttempt are now tracked per student in StudentTaskProgress.
 */
public record TaskPortDTO(
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
