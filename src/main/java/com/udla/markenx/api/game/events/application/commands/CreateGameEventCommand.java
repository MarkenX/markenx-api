package com.udla.markenx.api.game.events.application.commands;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record CreateGameEventCommand(
        @Nullable String id,
        String title,
        String description,
        List<EventEffectDTO> effects
) {
    public CreateGameEventCommand(
            String title,
            String description,
            List<EventEffectDTO> effects
    ) {
        this(null, title, description, effects);
    }

    public record EventEffectDTO(
            String dimensionId,
            double weightMultiplier
    ) {
    }
}
