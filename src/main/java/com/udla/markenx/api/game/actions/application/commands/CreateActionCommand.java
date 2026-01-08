package com.udla.markenx.api.game.actions.application.commands;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public record CreateActionCommand(
        @Nullable String id,
        String name,
        String description,
        BigDecimal cost,
        String category,
        boolean isInitiallyLocked,
        @Nullable String prerequisiteActionId,
        List<ActionEffectDTO> effects
) {
    public CreateActionCommand(
            String name,
            String description,
            BigDecimal cost,
            String category,
            boolean isInitiallyLocked,
            @Nullable String prerequisiteActionId,
            List<ActionEffectDTO> effects
    ) {
        this(null, name, description, cost, category, isInitiallyLocked, prerequisiteActionId, effects);
    }

    public record ActionEffectDTO(
            String dimensionId,
            double delta
    ) {
    }
}
