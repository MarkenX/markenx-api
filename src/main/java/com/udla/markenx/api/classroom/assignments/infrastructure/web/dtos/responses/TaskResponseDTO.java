package com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        String id,
        String label,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        int maxAttempts,
        String courseId,
        String scenarioId,
        String lifecycleStatus
) {

    @Contract("_ -> new")
    public static @NonNull TaskResponseDTO from(@NonNull TaskPortDTO task) {
        return new TaskResponseDTO(
                task.id(),
                task.label(),
                task.title(),
                task.summary(),
                task.deadline(),
                task.minScoreToPass(),
                task.maxAttempts(),
                task.courseId(),
                task.scenarioId(),
                task.lifecycleStatus()
        );
    }

    public static @NonNull Page<TaskResponseDTO> from(@NonNull Page<TaskPortDTO> tasks) {
        return tasks.map(TaskResponseDTO::from);
    }
}
