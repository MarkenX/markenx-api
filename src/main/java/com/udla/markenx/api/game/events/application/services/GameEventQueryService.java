package com.udla.markenx.api.game.events.application.services;

import com.udla.markenx.api.game.events.application.ports.incoming.GameEventQueryUseCase;
import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import com.udla.markenx.api.game.events.domain.ports.outgoing.GameEventQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameEventQueryService implements GameEventQueryUseCase {

    private final GameEventQueryRepository repository;

    @Override
    public List<GameEvent> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<GameEvent> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<GameEvent> getByScenarioId(String scenarioId) {
        return repository.findByScenarioId(scenarioId);
    }

    @Override
    public List<EventEffect> getEffectsByEventId(String eventId) {
        return repository.findEffectsByEventId(eventId);
    }
}
