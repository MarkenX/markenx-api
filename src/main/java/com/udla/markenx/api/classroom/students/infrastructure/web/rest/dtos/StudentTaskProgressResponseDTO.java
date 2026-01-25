package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

/**
 * Response DTO for student task progress endpoint.
 *
 * @param studentId The ID of the student
 * @param taskId The ID of the task
 * @param currentAttempt The current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts
 * @param status The progress status (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)
 */
public record StudentTaskProgressResponseDTO(
        String studentId,
        String taskId,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts,
        String status
) {

    @Contract("_ -> new")
    public static @NonNull StudentTaskProgressResponseDTO from(@NonNull StudentTaskProgressPortDTO dto) {
        return new StudentTaskProgressResponseDTO(
                dto.studentId(),
                dto.taskId(),
                dto.currentAttempt(),
                dto.maxAttempts(),
                dto.remainingAttempts(),
                dto.status()
        );
    }
}
