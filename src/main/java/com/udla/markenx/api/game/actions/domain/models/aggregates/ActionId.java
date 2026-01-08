package com.udla.markenx.api.game.actions.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jspecify.annotations.NonNull;

public class ActionId extends Identifier {

    public ActionId(String value) {
        super(value);
    }

    public static @NonNull ActionId generate() {
        return new ActionId(java.util.UUID.randomUUID().toString());
    }
}
