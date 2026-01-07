package com.udla.markenx.api.game.scenarios.application.commands;

import java.math.BigDecimal;
import java.util.List;

public record CreateScenarioCommand(
        String title,
        String description,
        ConsumerDTO consumer,
        List<DimensionDTO> dimensions,
        List<ActionDTO> actions,
        List<EventDTO> events
) {
    public record ConsumerDTO(
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
    }

    public record DimensionDTO(
            String id,
            String name,
            String displayName,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
    }

    public record ActionDTO(
            String id,
            String name,
            String description,
            BigDecimal cost,
            String category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffectDTO> effects
    ) {
    }

    public record ActionEffectDTO(
            String dimensionId,
            double delta
    ) {
    }

    public record EventDTO(
            String id,
            String title,
            String description,
            List<EventEffectDTO> effects
    ) {
    }

    public record EventEffectDTO(
            String dimensionId,
            double weightMultiplier
    ) {
    }
}
