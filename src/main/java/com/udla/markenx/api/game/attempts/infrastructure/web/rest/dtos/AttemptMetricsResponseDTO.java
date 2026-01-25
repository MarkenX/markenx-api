package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for attempt metrics.
 * Used by GET /attempts/{id}/metrics endpoint.
 *
 * <p>Note: The outcome field only contains WIN or LOSE values,
 * as metrics are only available for completed attempts.</p>
 *
 * @param attemptId The attempt ID
 * @param taskId The associated task ID
 * @param profileDiscoveryPercentage Profile discovery percentage (0.0-1.0)
 * @param finalAcceptance Final acceptance rate (0.0-1.0)
 * @param remainingBudget Remaining budget at end of attempt
 * @param totalTurnsUsed Number of turns used
 * @param outcome The attempt outcome (WIN or LOSE only)
 * @param evaluatedAt When the attempt was evaluated
 */
public record AttemptMetricsResponseDTO(
        String attemptId,
        String taskId,
        double profileDiscoveryPercentage,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        AttemptOutcome outcome,
        LocalDateTime evaluatedAt
) {

    @Contract("_ -> new")
    public static @NonNull AttemptMetricsResponseDTO from(@NonNull GameSessionResponse response) {
        AttemptOutcome outcome = AttemptOutcome.fromCompleted(
                AttemptStatus.valueOf(response.finalOutcome())
        );

        return new AttemptMetricsResponseDTO(
                response.id(),
                response.taskId(),
                response.profileDiscoveryPercentage(),
                response.finalAcceptance(),
                response.remainingBudget(),
                response.totalTurnsUsed(),
                outcome,
                response.sessionDate()
        );
    }
}
