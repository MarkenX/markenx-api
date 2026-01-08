package com.udla.markenx.api.game.events.application.ports.incoming;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;

import java.util.List;
import java.util.Optional;

public interface GameEventQueryUseCase {
    List<GameEvent> getAll();
    Optional<GameEvent> getById(String id);
    List<GameEvent> getByScenarioId(String scenarioId);
    List<EventEffect> getEffectsByEventId(String eventId);
}
