package com.udla.markenx.api.game.dimensions.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class DimensionId extends Identifier {
    protected DimensionId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull DimensionId generate() {
        return new DimensionId(java.util.UUID.randomUUID().toString());
    }
}
