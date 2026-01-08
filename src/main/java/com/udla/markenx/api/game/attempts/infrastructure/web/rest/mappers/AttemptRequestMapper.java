package com.udla.markenx.api.game.attempts.infrastructure.web.rest.mappers;

import com.udla.markenx.api.game.attempts.application.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.RegisterGameSessionRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttemptRequestMapper {

    public RegisterGameSessionCommand toCommand(RegisterGameSessionRequestDTO dto) {
        List<RegisterGameSessionCommand.TurnHistoryDTO> history = dto.history() != null
                ? dto.history().stream()
                    .map(th -> new RegisterGameSessionCommand.TurnHistoryDTO(
                            th.turnNumber(),
                            th.acceptanceAtEnd(),
                            th.budgetAtEnd(),
                            th.eventOccurredTitle(),
                            th.actionsTakenIds()
                    ))
                    .collect(Collectors.toList())
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
