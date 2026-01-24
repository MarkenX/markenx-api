package com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos;

import java.time.LocalDateTime;

/**
 * Response DTO for task attempts.
 * Used by GET /tasks/{id}/attempts endpoint.
 */
public record TaskAttemptResponseDTO(
        String attemptId,
        String taskId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String status,
        String outcome,
        double score
) {
}
