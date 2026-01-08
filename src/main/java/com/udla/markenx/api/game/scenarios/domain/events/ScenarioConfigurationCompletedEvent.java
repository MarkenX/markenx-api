package com.udla.markenx.api.game.scenarios.domain.events;

import java.util.List;

public record ScenarioConfigurationCompletedEvent(
        String scenarioId,
        String consumerId,
        List<String> dimensionIds,
        List<String> actionIds,
        List<String> eventIds
) {
}
