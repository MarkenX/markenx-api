package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import java.time.LocalDateTime;

/**
 * Response DTO for a task with student-specific progress.
 *
 * @param id The ID of the task
 * @param label The task label (e.g., "TSK-0001")
 * @param title The task title
 * @param summary The task summary
 * @param deadline The task deadline
 * @param minScoreToPass The minimum score required to pass (0.0-1.0)
 * @param status The task status
 * @param currentAttempt The student's current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts for the student
 */
public record StudentTaskWithProgressResponseDTO(
        String id,
        String label,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        String status,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts
) {
}
