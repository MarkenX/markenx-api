package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for attempt metrics.
 * Used by GET /attempts/{attemptId}/metrics endpoint.
 */
public record AttemptMetricsResponseDTO(
        String attemptId,
        String taskId,
        double profileDiscoveryPercentage,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        String finalOutcome,
        LocalDateTime evaluatedAt
) {

    @Contract("_ -> new")
    public static @NonNull AttemptMetricsResponseDTO from(@NonNull GameSessionResponse response) {
        return new AttemptMetricsResponseDTO(
                response.id(),
                response.taskId(),
                response.profileDiscoveryPercentage(),
                response.finalAcceptance(),
                response.remainingBudget(),
                response.totalTurnsUsed(),
                response.finalOutcome(),
                response.sessionDate()
        );
    }
}
