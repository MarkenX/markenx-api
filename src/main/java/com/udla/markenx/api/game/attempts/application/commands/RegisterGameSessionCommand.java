package com.udla.markenx.api.game.attempts.application.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RegisterGameSessionCommand(
        String taskId,
        String studentId,
        LocalDateTime sessionDate,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        double profileDiscoveryPercentage,
        List<TurnHistoryDTO> history
) {
    public record TurnHistoryDTO(
            int turnNumber,
            double acceptanceAtEnd,
            BigDecimal budgetAtEnd,
            String eventOccurredTitle,
            List<String> actionsTakenIds
    ) {
    }
}
