package com.udla.markenx.api.game.attempts.domain.ports.outgoing;

import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;

import java.util.List;

public interface AttemptCommandRepository {
    Attempt save(Attempt attempt);
    void saveTurnHistories(String attemptId, List<TurnHistory> turnHistories);
    void saveTurnActions(String turnHistoryId, List<String> actionIds);
}
