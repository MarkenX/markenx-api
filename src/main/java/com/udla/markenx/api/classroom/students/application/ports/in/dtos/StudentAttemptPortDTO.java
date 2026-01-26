package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

import java.time.LocalDateTime;

public record StudentAttemptPortDTO(
        String attemptId,
        String taskId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String status,
        String outcome,
        double score
) {
}
