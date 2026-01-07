package com.udla.markenx.api.game.dimensions.application.commands;

import org.jspecify.annotations.Nullable;

public record CreateDimensionCommand(
        @Nullable String id,
        String name,
        String displayName,
        String description,
        double consumerExpectation,
        double productInitialOffer
) {
    public CreateDimensionCommand(
            String name,
            String displayName,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
        this(null, name, displayName, description, consumerExpectation, productInitialOffer);
    }
}
