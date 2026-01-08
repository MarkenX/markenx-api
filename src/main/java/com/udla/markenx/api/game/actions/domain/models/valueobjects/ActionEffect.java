package com.udla.markenx.api.game.actions.domain.models.valueobjects;

import com.udla.markenx.api.game.actions.domain.exceptions.InvalidActionEffectDeltaException;
import com.udla.markenx.api.game.actions.domain.exceptions.InvalidDimensionReferenceException;

public record ActionEffect(
        String id,
        String dimensionId,
        double delta
) {
    public ActionEffect {
        if (dimensionId == null || dimensionId.isBlank()) {
            throw new InvalidDimensionReferenceException();
        }
        if (delta < -1.0 || delta > 1.0) {
            throw new InvalidActionEffectDeltaException();
        }
    }

    public ActionEffect(String dimensionId, double delta) {
        this(java.util.UUID.randomUUID().toString(), dimensionId, delta);
    }
}
