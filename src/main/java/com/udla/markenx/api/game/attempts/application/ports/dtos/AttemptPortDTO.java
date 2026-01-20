package com.udla.markenx.api.game.attempts.application.ports.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AttemptPortDTO(
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
