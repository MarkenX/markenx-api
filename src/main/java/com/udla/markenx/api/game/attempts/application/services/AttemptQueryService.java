package com.udla.markenx.api.game.attempts.application.services;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.ports.in.mappers.AttemptPortMapper;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import com.udla.markenx.api.game.attempts.domain.exceptions.AttemptNotFoundException;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.application.ports.out.AttemptQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptQueryService implements AttemptQueryUseCase {

    private final AttemptQueryRepository repository;
    private final AttemptPortMapper mapper = new AttemptPortMapper();

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

    @Override
    public List<AttemptPortDTO> getByTaskId(String taskId) {
        return repository.findByTaskId(taskId).stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<AttemptPortDTO> getByStudentId(String studentId) {
        return repository.findByStudentId(studentId).stream().map(mapper::toDTO).toList();
    }
}
