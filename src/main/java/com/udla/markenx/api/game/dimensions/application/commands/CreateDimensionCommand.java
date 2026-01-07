package com.udla.markenx.api.game.dimensions.application.commands;

public record CreateDimensionCommand(
        String name,
        String displayName,
        String description,
        double consumerExpectation,
        double productInitialOffer
) {
}
