package com.udla.markenx.api.game.scenarios.application.commands;

public record SaveScenarioCommand(
        String title,
        String description
) {
}
