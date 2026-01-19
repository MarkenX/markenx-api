package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import java.time.LocalDateTime;

/**
 * Response DTO for student attempts.
 * Used by GET /students/{id}/attempts endpoint.
 */
public record StudentAttemptResponseDTO(
        String attemptId,
        String taskId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String status,
        String outcome,
        double score
) {
}
