package com.udla.markenx.api.game.attempts.application.ports.in.usecases;

import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.GameSessionResponse;

public interface RegisterGameSessionUseCase {
    GameSessionResponse handle(RegisterGameSessionCommand command);
}
