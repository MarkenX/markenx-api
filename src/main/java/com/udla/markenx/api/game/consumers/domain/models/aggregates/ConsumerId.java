package com.udla.markenx.api.game.consumers.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jspecify.annotations.NonNull;

public class ConsumerId extends Identifier {

    public ConsumerId(String value) {
        super(value);
    }

    public static @NonNull ConsumerId generate() {
        return new ConsumerId(java.util.UUID.randomUUID().toString());
    }
}
