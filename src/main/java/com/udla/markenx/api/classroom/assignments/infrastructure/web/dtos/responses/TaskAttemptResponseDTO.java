package com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.responses;

import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskAttemptQueryPort.TaskAttemptData;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public record TaskAttemptResponseDTO(
        String attemptId,
        String taskId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String status,
        String outcome,
        double score
) {

    @Contract("_ -> new")
    public static @NonNull TaskAttemptResponseDTO from(@NonNull TaskAttemptData data) {
        return new TaskAttemptResponseDTO(
                data.attemptId(),
                data.taskId(),
                data.evaluatedAt(),
                data.evaluatedAt(),
                data.status(),
                data.finalOutcome(),
                data.finalAcceptance()
        );
    }

    public static @NonNull List<TaskAttemptResponseDTO> from(@NonNull List<TaskAttemptData> list) {
        return list.stream().map(TaskAttemptResponseDTO::from).toList();
    }
}
