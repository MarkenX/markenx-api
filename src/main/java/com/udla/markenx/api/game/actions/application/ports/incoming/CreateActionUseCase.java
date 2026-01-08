package com.udla.markenx.api.game.actions.application.ports.incoming;

import com.udla.markenx.api.game.actions.application.commands.CreateActionCommand;
import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;

public interface CreateActionUseCase {
    Action handle(CreateActionCommand command);
}
