package com.udla.markenx.api.game.actions.application.services;

import com.udla.markenx.api.game.actions.application.ports.incoming.ActionQueryUseCase;
import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import com.udla.markenx.api.game.actions.domain.ports.outgoing.ActionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionQueryService implements ActionQueryUseCase {

    private final ActionQueryRepository repository;

    @Override
    public List<Action> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Action> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Action> getByScenarioId(String scenarioId) {
        return repository.findByScenarioId(scenarioId);
    }

    @Override
    public List<ActionEffect> getEffectsByActionId(String actionId) {
        return repository.findEffectsByActionId(actionId);
    }
}
