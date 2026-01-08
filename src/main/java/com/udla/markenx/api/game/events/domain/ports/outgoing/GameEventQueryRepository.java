package com.udla.markenx.api.game.events.domain.ports.outgoing;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;

import java.util.List;
import java.util.Optional;

public interface GameEventQueryRepository {
    List<GameEvent> findAll();
    Optional<GameEvent> findById(String id);
    List<GameEvent> findByScenarioId(String scenarioId);
    List<EventEffect> findEffectsByEventId(String eventId);
}
