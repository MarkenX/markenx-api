package com.udla.markenx.api.game.events.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jspecify.annotations.NonNull;

public class GameEventId extends Identifier {

    public GameEventId(String value) {
        super(value);
    }

    public static @NonNull GameEventId generate() {
        return new GameEventId(java.util.UUID.randomUUID().toString());
    }
}
