package com.udla.markenx.api.game.attempts.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record GameSessionResponse(
        String id,
        String taskId,
        String studentId,
        LocalDateTime sessionDate,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        double profileDiscoveryPercentage,
        String finalOutcome,
        List<TurnHistoryResponse> history
) {
    public record TurnHistoryResponse(
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
    }
}
