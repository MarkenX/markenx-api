package com.udla.markenx.api.game.actions.domain.ports.outgoing;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;

import java.util.List;
import java.util.Optional;

public interface ActionQueryRepository {
    List<Action> findAll();
    Optional<Action> findById(String id);
    List<Action> findByScenarioId(String scenarioId);
    List<ActionEffect> findEffectsByActionId(String actionId);
}
