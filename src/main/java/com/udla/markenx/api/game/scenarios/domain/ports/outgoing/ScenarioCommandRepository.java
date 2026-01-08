package com.udla.markenx.api.game.scenarios.domain.ports.outgoing;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;

import java.util.List;

public interface ScenarioCommandRepository {
    Scenario save(Scenario scenario);
    Scenario findById(String id);
    void associateConsumer(String scenarioId, String consumerId);
    void addDimensions(String scenarioId, List<String> dimensionIds);
    void addActions(String scenarioId, List<String> actionIds);
    void addEvents(String scenarioId, List<String> eventIds);
}
