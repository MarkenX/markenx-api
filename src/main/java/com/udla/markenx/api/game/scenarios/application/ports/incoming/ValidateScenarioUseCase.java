package com.udla.markenx.api.game.scenarios.application.ports.incoming;

import com.udla.markenx.api.game.scenarios.application.queries.ScenarioExistsQuery;

public interface ValidateScenarioUseCase {
    boolean exists(ScenarioExistsQuery query);
}
