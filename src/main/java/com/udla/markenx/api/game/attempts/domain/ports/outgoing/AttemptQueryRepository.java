package com.udla.markenx.api.game.attempts.domain.ports.outgoing;

import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;

import java.util.List;
import java.util.Optional;

public interface AttemptQueryRepository {
    Optional<Attempt> findById(String id);
    List<TurnHistory> findTurnHistoriesByAttemptId(String attemptId);
    List<String> findActionIdsByTurnHistoryId(String turnHistoryId);
}
