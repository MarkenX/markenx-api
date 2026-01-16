package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

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
}
