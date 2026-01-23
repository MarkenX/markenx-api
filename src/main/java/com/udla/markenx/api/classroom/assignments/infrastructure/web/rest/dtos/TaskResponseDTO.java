package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos;

import java.time.LocalDateTime;

/**
 * Response DTO for Task data.
 * Note: status and currentAttempt are now tracked per student via GET /students/{studentId}/tasks/{id}/progress
 */
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
