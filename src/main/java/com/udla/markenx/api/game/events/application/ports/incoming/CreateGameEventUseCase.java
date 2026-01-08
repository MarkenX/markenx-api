package com.udla.markenx.api.game.events.application.ports.incoming;

import com.udla.markenx.api.game.events.application.commands.CreateGameEventCommand;
import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;

public interface CreateGameEventUseCase {
    GameEvent handle(CreateGameEventCommand command);
}
