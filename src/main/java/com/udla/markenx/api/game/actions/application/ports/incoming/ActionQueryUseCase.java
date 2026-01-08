package com.udla.markenx.api.game.actions.application.ports.incoming;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;

import java.util.List;
import java.util.Optional;

public interface ActionQueryUseCase {
    List<Action> getAll();
    Optional<Action> getById(String id);
    List<Action> getByScenarioId(String scenarioId);
    List<ActionEffect> getEffectsByActionId(String actionId);
}
