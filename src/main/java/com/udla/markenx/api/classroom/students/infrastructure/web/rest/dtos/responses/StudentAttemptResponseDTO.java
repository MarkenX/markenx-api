package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for student attempts.
 * Used by GET /students/{attemptId}/attempts endpoint.
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

    @Contract("_ -> new")
    public static @NonNull StudentAttemptResponseDTO from(@NonNull StudentAttemptPortDTO dto) {
        return new StudentAttemptResponseDTO(
                dto.attemptId(),
                dto.taskId(),
                dto.startedAt(),
                dto.finishedAt(),
                dto.status(),
                dto.outcome(),
                dto.score()
        );
    }

    public static @NonNull List<StudentAttemptResponseDTO> from(@NonNull List<StudentAttemptPortDTO> list) {
        return list.stream().map(StudentAttemptResponseDTO::from).toList();
    }
}
