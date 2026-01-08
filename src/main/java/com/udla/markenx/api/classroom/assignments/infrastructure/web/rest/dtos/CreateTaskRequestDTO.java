package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos;

import java.time.LocalDateTime;

public record CreateTaskRequestDTO(
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        int maxAttempts,
        String courseId
) {
}
