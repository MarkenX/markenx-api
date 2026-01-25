package com.udla.markenx.api.game.attempts.application.ports.in.commands;

import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.RegisterGameSessionRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

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

    @Contract("_ -> new")
    public static @NonNull RegisterGameSessionCommand from(@NonNull RegisterGameSessionRequestDTO dto) {
        List<TurnHistoryDTO> history = dto.history() != null
                ? dto.history().stream()
                    .map(th -> new TurnHistoryDTO(
                            th.turnNumber(),
                            th.acceptanceAtEnd(),
                            th.budgetAtEnd(),
                            th.eventOccurredTitle(),
                            th.actionsTakenIds()
                    ))
                    .toList()
                : List.of();

        return new RegisterGameSessionCommand(
                dto.taskId(),
                dto.studentId(),
                dto.sessionDate(),
                dto.finalAcceptance(),
                dto.remainingBudget(),
                dto.totalTurnsUsed(),
                dto.profileDiscoveryPercentage(),
                history
        );
    }
}
