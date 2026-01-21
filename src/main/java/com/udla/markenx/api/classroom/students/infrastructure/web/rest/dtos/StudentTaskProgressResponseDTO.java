package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

/**
 * Response DTO for student task progress endpoint.
 *
 * @param studentId The ID of the student
 * @param taskId The ID of the task
 * @param currentAttempt The current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts
 */
public record StudentTaskProgressResponseDTO(
        String studentId,
        String taskId,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts
) {
}
