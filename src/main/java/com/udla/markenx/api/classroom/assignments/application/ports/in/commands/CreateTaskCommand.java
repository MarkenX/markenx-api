package com.udla.markenx.api.classroom.assignments.application.ports.in.commands;

import com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.CreateTaskRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public record CreateTaskCommand(
        String title,
        String summary,
        LocalDateTime deadline,
        Double minScoreToPass,
        String courseId,
        int maxAttempts,
        String scenarioId,
        boolean isHistorical
) {

    @Contract("_ -> new")
    public static @NonNull CreateTaskCommand from(@NonNull CreateTaskRequestDTO request) {
        return new CreateTaskCommand(
                request.title(),
                request.summary(),
                request.deadline(),
                request.minScoreToPass(),
                request.courseId(),
                request.maxAttempts(),
                request.scenarioId(),
                false
        );
    }
}
