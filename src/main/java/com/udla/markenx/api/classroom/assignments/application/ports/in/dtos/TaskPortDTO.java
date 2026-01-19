package com.udla.markenx.api.classroom.assignments.application.ports.in.dtos;

import java.time.LocalDateTime;

public record TaskPortDTO(
        String id,
        String label,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        String status,
        int maxAttempts,
        int currentAttempt,
        String courseId
) {
}
