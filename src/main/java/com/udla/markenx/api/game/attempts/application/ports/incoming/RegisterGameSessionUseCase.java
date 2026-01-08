package com.udla.markenx.api.game.attempts.application.ports.incoming;

import com.udla.markenx.api.game.attempts.application.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;

public interface RegisterGameSessionUseCase {
    GameSessionResponse handle(RegisterGameSessionCommand command);
}
