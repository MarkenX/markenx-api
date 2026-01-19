package com.udla.markenx.api.classroom.assignments.application.ports.in.commands;

import java.time.LocalDateTime;

public record CreateTaskCommand(
        String title,
        String summary,
        LocalDateTime deadline,
        Double minScoreToPass,
        String courseId,
        int maxAttempts,
        boolean isHistorical
) {
}
