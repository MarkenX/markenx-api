package com.udla.markenx.api.game.events.application.commands;

import java.util.List;

public record CreateGameEventCommand(
        String title,
        String description,
        List<EventEffectDTO> effects
) {
    public record EventEffectDTO(
            String dimensionId,
            double weightMultiplier
    ) {
    }
}
