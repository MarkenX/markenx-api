package com.udla.markenx.api.game.attempts.application.services;

import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.TaskScoreProvider;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import com.udla.markenx.api.game.attempts.application.ports.out.AttemptCommandRepository;
import com.udla.markenx.api.shared.domain.events.integration.AttemptResultRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterGameSessionCommandHandler implements RegisterGameSessionUseCase {

    private final AttemptCommandRepository repository;
    private final TaskScoreProvider taskScoreProvider;
    private final ApplicationEventPublisher eventPublisher;

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

        // 7. Publish event to update task attempt counter and status
        publishAttemptResultEvent(savedAttempt);

        // 8. Map to response
        return mapToResponse(savedAttempt);
    }

    private void publishAttemptResultEvent(Attempt attempt) {
        var event = new AttemptResultRegisteredEvent(
                attempt.getId(),
                attempt.getTaskId(),
                attempt.getStudentId(),
                attempt.getResult().profileScore(),
                attempt.getStatus() == AttemptStatus.APPROVED
        );

        log.info("Publishing AttemptResultRegisteredEvent: attemptId={}, taskId={}, approved={}",
                event.attemptId(), event.taskId(), event.isApproved());

        eventPublisher.publishEvent(event);
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
