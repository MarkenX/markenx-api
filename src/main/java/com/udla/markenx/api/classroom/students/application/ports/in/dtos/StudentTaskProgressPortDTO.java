package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

/**
 * Port DTO representing a student's progress on a specific task.
 *
 * @param studentId The ID of the student
 * @param taskId The ID of the task
 * @param currentAttempt The current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts
 * @param status The progress status (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)
 */
public record StudentTaskProgressPortDTO(
        String studentId,
        String taskId,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts,
        String status
) {
}
