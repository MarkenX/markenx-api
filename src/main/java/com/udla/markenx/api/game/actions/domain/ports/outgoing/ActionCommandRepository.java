package com.udla.markenx.api.game.actions.domain.ports.outgoing;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;

import java.util.List;

public interface ActionCommandRepository {
    Action save(Action action);
    Action findById(String id);
    void saveEffects(String actionId, List<ActionEffect> effects);
}
