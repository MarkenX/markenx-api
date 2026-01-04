package com.udla.markenx.api.attempts.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class AttemptId extends Identifier {
    protected AttemptId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull AttemptId generate() {
        return new AttemptId(java.util.UUID.randomUUID().toString());
    }
}
