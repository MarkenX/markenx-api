package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record GameSessionResponseDTO(
        String id,
        String taskId,
        String studentId,
        LocalDateTime sessionDate,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        double profileDiscoveryPercentage,
        String finalOutcome,
        List<TurnHistoryResponseDTO> history
) {
    public record TurnHistoryResponseDTO(
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
    }
}
