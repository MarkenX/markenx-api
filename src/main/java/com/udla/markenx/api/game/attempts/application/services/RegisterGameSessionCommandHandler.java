package com.udla.markenx.api.game.attempts.application.services;

import com.udla.markenx.api.game.attempts.application.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.incoming.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.application.ports.incoming.TaskScoreProvider;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.domain.ports.outgoing.AttemptCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterGameSessionCommandHandler implements RegisterGameSessionUseCase {

    private final AttemptCommandRepository repository;
    private final TaskScoreProvider taskScoreProvider;

    @Override
    @Transactional
    public GameSessionResponse handle(@NonNull RegisterGameSessionCommand command) {
        // 1. Get minScoreToPass from Task module
        double minScoreToPass = taskScoreProvider.getMinScoreToPass(command.taskId());

        // 2. Build TurnHistory entities from command
        List<TurnHistory> turnHistories = command.history() != null
                ? command.history().stream()
                    .map(dto -> TurnHistory.create(
                            dto.turnNumber(),
                            dto.acceptanceAtEnd(),
                            dto.budgetAtEnd(),
                            dto.eventOccurredTitle(),
                            dto.actionsTakenIds()
                    ))
                    .collect(Collectors.toList())
                : List.of();

        // 3. Create Attempt with results (status calculated internally)
        Attempt attempt = Attempt.createWithResults(
                command.taskId(),
                command.studentId(),
                command.sessionDate(),
                command.finalAcceptance(),
                command.remainingBudget(),
                command.totalTurnsUsed(),
                command.profileDiscoveryPercentage(),
                turnHistories,
                minScoreToPass
        );

        // 4. Persist attempt
        Attempt savedAttempt = repository.save(attempt);

        // 5. Persist turn histories
        if (!turnHistories.isEmpty()) {
            repository.saveTurnHistories(savedAttempt.getId(), turnHistories);

            // 6. Persist turn actions for each turn
            for (TurnHistory turnHistory : turnHistories) {
                if (!turnHistory.getActionsTakenIds().isEmpty()) {
                    repository.saveTurnActions(turnHistory.getId(), turnHistory.getActionsTakenIds());
                }
            }
        }

        // 7. Map to response
        return mapToResponse(savedAttempt);
    }

    private GameSessionResponse mapToResponse(Attempt attempt) {
        List<GameSessionResponse.TurnHistoryResponse> historyResponses =
                attempt.getTurnHistories().stream()
                        .map(th -> new GameSessionResponse.TurnHistoryResponse(
                                th.getTurnNumber(),
                                th.getAcceptanceAtEnd(),
                                th.getBudgetAtEnd(),
                                th.getEventOccurredTitle(),
                                th.getActionsTakenIds()
                        ))
                        .collect(Collectors.toList());

        return new GameSessionResponse(
                attempt.getId(),
                attempt.getTaskId(),
                attempt.getStudentId(),
                attempt.getSessionDate(),
                attempt.getResult().approvalRate(),
                attempt.getResult().budgetRemaining(),
                attempt.getResult().currentTurn(),
                attempt.getResult().profileScore(),
                attempt.getStatus().name(),
                historyResponses
        );
    }
}
