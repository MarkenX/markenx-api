package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class ScenarioId extends Identifier {
    public ScenarioId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull ScenarioId generate() {
        return new ScenarioId(java.util.UUID.randomUUID().toString());
    }
}
