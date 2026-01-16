package com.udla.markenx.api.shared.domain.events.integration;

/**
 * Integration event published when a game session attempt result has been registered.
 * This event crosses module boundaries (game/attempts -> classroom/assignments).
 *
 * @param attemptId The ID of the registered attempt
 * @param taskId The ID of the task associated with the attempt
 * @param studentId The ID of the student who made the attempt
 * @param profileScore The profile discovery score achieved (0.0-1.0)
 * @param isApproved Whether the attempt was approved based on minScoreToPass
 */
public record AttemptResultRegisteredEvent(
        String attemptId,
        String taskId,
        String studentId,
        double profileScore,
        boolean isApproved
) {
}
