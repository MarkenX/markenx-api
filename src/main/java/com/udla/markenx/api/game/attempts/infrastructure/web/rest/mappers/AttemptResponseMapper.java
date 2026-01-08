package com.udla.markenx.api.game.attempts.infrastructure.web.rest.mappers;

import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.GameSessionResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttemptResponseMapper {

    public GameSessionResponseDTO toDTO(GameSessionResponse response) {
        List<GameSessionResponseDTO.TurnHistoryResponseDTO> history = response.history() != null
                ? response.history().stream()
                    .map(th -> new GameSessionResponseDTO.TurnHistoryResponseDTO(
                            th.turnNumber(),
                            th.acceptanceAtEnd(),
                            th.budgetAtEnd(),
                            th.eventOccurredTitle(),
                            th.actionsTakenIds()
                    ))
                    .collect(Collectors.toList())
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
