package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

import java.time.LocalDateTime;

/**
 * Port DTO representing a task with the student's specific progress.
 *
 * @param taskId The ID of the task
 * @param taskLabel The task label (e.g., "TSK-0001")
 * @param title The task title
 * @param summary The task summary
 * @param deadline The task deadline
 * @param minScoreToPass The minimum score required to pass (0.0-1.0)
 * @param status The task status (AssignmentStatus name)
 * @param currentAttempt The student's current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts for the student
 * @param scenarioId The ID of the scenario associated with the task
 */
public record StudentTaskPortDTO(
        String taskId,
        String taskLabel,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        String status,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts,
        String scenarioId
) {
}
