package com.udla.markenx.api.game.scenarios.domain.ports.outgoing;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;

public interface ScenarioCommandRepository {
    Scenario save(Scenario scenario);
    Scenario findById(String id);
}
