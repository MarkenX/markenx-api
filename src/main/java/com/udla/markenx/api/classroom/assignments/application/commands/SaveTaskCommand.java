package com.udla.markenx.api.classroom.assignments.application.commands;

import java.time.LocalDateTime;

public record SaveTaskCommand(
        String title,
        String summary,
        LocalDateTime deadline,
        Double minScoreToPass,
        String courseId,
        int maxAttempts,
        boolean isHistorical
) {
}
