package com.udla.markenx.api.game.scenarios.domain.events;

public record ScenarioCreationFailedEvent(
        String scenarioId,
        String reason
) {
}
