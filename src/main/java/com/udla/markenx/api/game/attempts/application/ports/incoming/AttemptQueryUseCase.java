package com.udla.markenx.api.game.attempts.application.ports.incoming;

import com.udla.markenx.api.game.attempts.application.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;

public interface AttemptQueryUseCase {
    GameSessionResponse getById(GetAttemptByIdQuery query);
}
