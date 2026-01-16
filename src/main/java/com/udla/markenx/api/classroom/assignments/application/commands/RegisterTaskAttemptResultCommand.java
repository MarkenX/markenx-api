package com.udla.markenx.api.classroom.assignments.application.commands;

/**
 * Command to register an attempt result for a task.
 * Updates the task's currentAttempt counter and status based on the score.
 *
 * @param taskId The ID of the task
 * @param score The score achieved in the attempt (0.0-1.0)
 */
public record RegisterTaskAttemptResultCommand(
        String taskId,
        double score
) {
}
