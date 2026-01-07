package com.udla.markenx.api.game.scenarios.infrastructure.web.rest.dtos;

import java.math.BigDecimal;
import java.util.List;

public record CreateScenarioRequestDTO(
        String title,
        String description,
        ConsumerRequestDTO consumer,
        List<DimensionRequestDTO> dimensions,
        List<ActionRequestDTO> actions,
        List<EventRequestDTO> events
) {
    public record ConsumerRequestDTO(
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
    }

    public record DimensionRequestDTO(
            String id,
            String name,
            String displayName,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
    }

    public record ActionRequestDTO(
            String id,
            String name,
            String description,
            BigDecimal cost,
            String category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffectRequestDTO> effects
    ) {
    }

    public record ActionEffectRequestDTO(
            String dimensionId,
            double delta
    ) {
    }

    public record EventRequestDTO(
            String id,
            String title,
            String description,
            List<EventEffectRequestDTO> effects
    ) {
    }

    public record EventEffectRequestDTO(
            String dimensionId,
            double weightMultiplier
    ) {
    }
}
