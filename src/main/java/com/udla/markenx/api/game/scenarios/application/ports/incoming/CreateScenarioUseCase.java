package com.udla.markenx.api.game.scenarios.application.ports.incoming;

import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioResponse;

public interface CreateScenarioUseCase {
    ScenarioResponse handle(CreateScenarioCommand command);
}
