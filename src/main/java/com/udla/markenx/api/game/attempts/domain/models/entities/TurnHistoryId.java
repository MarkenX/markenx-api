package com.udla.markenx.api.game.attempts.domain.models.entities;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class TurnHistoryId extends Identifier {
    public TurnHistoryId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull TurnHistoryId generate() {
        return new TurnHistoryId(UUID.randomUUID().toString());
    }
}
