package com.udla.markenx.api.game.events.domain.models.valueobjects;

import com.udla.markenx.api.game.events.domain.exceptions.InvalidDimensionReferenceException;
import com.udla.markenx.api.game.events.domain.exceptions.InvalidWeightMultiplierException;

public record EventEffect(
        String id,
        String dimensionId,
        double weightMultiplier
) {
    public EventEffect {
        if (dimensionId == null || dimensionId.isBlank()) {
            throw new InvalidDimensionReferenceException();
        }
        if (weightMultiplier < 0) {
            throw new InvalidWeightMultiplierException();
        }
    }

    public EventEffect(String dimensionId, double weightMultiplier) {
        this(java.util.UUID.randomUUID().toString(), dimensionId, weightMultiplier);
    }
}
