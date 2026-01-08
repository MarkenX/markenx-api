package com.udla.markenx.api.game.scenarios.domain.events;

public record ScenarioCreatedEvent(
        String scenarioId,
        String title,
        String description
) {
}
