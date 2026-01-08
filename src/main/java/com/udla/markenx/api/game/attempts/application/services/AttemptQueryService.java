package com.udla.markenx.api.game.attempts.application.services;

import com.udla.markenx.api.game.attempts.application.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.incoming.AttemptQueryUseCase;
import com.udla.markenx.api.game.attempts.domain.exceptions.AttemptNotFoundException;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.domain.ports.outgoing.AttemptQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptQueryService implements AttemptQueryUseCase {

    private final AttemptQueryRepository repository;

    @Override
    public GameSessionResponse getById(@NonNull GetAttemptByIdQuery query) {
        // 1. Find attempt
        Attempt attempt = repository.findById(query.attemptId())
                .orElseThrow(() -> new AttemptNotFoundException(query.attemptId()));

        // 2. Find turn histories
        List<TurnHistory> turnHistories = repository.findTurnHistoriesByAttemptId(query.attemptId());

        // 3. For each turn history, find actions
        List<GameSessionResponse.TurnHistoryResponse> historyResponses = turnHistories.stream()
                .map(th -> {
                    List<String> actionIds = repository.findActionIdsByTurnHistoryId(th.getId());
                    return new GameSessionResponse.TurnHistoryResponse(
                            th.getTurnNumber(),
                            th.getAcceptanceAtEnd(),
                            th.getBudgetAtEnd(),
                            th.getEventOccurredTitle(),
                            actionIds
                    );
                })
                .collect(Collectors.toList());

        // 4. Map to response
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
