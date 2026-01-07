package com.udla.markenx.api.game.actions.application.commands;

import java.math.BigDecimal;
import java.util.List;

public record CreateActionCommand(
        String name,
        String description,
        BigDecimal cost,
        String category,
        boolean isInitiallyLocked,
        String prerequisiteActionId,
        List<ActionEffectDTO> effects
) {
    public record ActionEffectDTO(
            String dimensionId,
            double delta
    ) {
    }
}
