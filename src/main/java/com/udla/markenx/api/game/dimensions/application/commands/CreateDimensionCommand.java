package com.udla.markenx.api.game.dimensions.application.commands;

public record CreateDimensionCommand(
        String name,
        String description,
        double consumerExpectation,
        double productInitialOffer
) {
}
