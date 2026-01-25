package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

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

    @Contract("_ -> new")
    public static @NonNull GameSessionResponseDTO from(@NonNull GameSessionResponse response) {
        List<TurnHistoryResponseDTO> history = response.history() != null
                ? response.history().stream()
                    .map(th -> new TurnHistoryResponseDTO(
                            th.turnNumber(),
                            th.acceptanceAtEnd(),
                            th.budgetAtEnd(),
                            th.eventOccurredTitle(),
                            th.actionsTakenIds()
                    ))
                    .toList()
                : List.of();

        return new GameSessionResponseDTO(
                response.id(),
                response.taskId(),
                response.studentId(),
                response.sessionDate(),
                response.finalAcceptance(),
                response.remainingBudget(),
                response.totalTurnsUsed(),
                response.profileDiscoveryPercentage(),
                response.finalOutcome(),
                history
        );
    }
}
