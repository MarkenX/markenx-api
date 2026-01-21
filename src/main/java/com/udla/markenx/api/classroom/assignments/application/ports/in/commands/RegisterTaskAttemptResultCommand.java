package com.udla.markenx.api.classroom.assignments.application.ports.in.commands;

/**
 * Command to register an attempt result for a task.
 * Updates the student's progress (currentAttempt) and task status based on the score.
 *
 * @param taskId The ID of the task
 * @param studentId The ID of the student who made the attempt
 * @param score The score achieved in the attempt (0.0-1.0)
 */
public record RegisterTaskAttemptResultCommand(
        String taskId,
        String studentId,
        double score
) {
}
