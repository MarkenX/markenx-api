package com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for a game session (attempt).
 *
 * @param id The attempt ID
 * @param taskId The associated task ID
 * @param studentId The student ID
 * @param sessionDate When the session occurred
 * @param finalAcceptance The final acceptance rate (0.0-1.0)
 * @param remainingBudget The remaining budget at end of session
 * @param totalTurnsUsed Number of turns used
 * @param profileDiscoveryPercentage Profile discovery percentage (0.0-1.0)
 * @param outcome The attempt outcome (WIN, LOSE, or IN_PROGRESS)
 * @param history Turn-by-turn history
 */
public record GameSessionResponseDTO(
        String id,
        String taskId,
        String studentId,
        LocalDateTime sessionDate,
        double finalAcceptance,
        BigDecimal remainingBudget,
        int totalTurnsUsed,
        double profileDiscoveryPercentage,
        AttemptOutcome outcome,
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

        AttemptOutcome outcome = AttemptOutcome.from(
                AttemptStatus.valueOf(response.finalOutcome())
        );

        return new GameSessionResponseDTO(
                response.id(),
                response.taskId(),
                response.studentId(),
                response.sessionDate(),
                response.finalAcceptance(),
                response.remainingBudget(),
                response.totalTurnsUsed(),
                response.profileDiscoveryPercentage(),
                outcome,
                history
        );
    }
}
