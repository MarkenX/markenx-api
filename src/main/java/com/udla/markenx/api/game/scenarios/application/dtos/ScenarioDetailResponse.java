package com.udla.markenx.api.game.scenarios.application.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ScenarioDetailResponse(
        String id,
        String title,
        String description,
        ConsumerResponse consumer,
        List<DimensionResponse> dimensions,
        List<ActionResponse> actions,
        List<EventResponse> events
) {
    public record ConsumerResponse(
            String id,
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
    }

    public record DimensionResponse(
            String id,
            String name,
            String displayName,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
    }

    public record ActionResponse(
            String id,
            String name,
            String description,
            BigDecimal cost,
            String category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffectResponse> effects
    ) {
    }

    public record ActionEffectResponse(
            String dimensionId,
            double delta
    ) {
    }

    public record EventResponse(
            String id,
            String title,
            String description,
            List<EventEffectResponse> effects
    ) {
    }

    public record EventEffectResponse(
            String dimensionId,
            double weightMultiplier
    ) {
    }
}
