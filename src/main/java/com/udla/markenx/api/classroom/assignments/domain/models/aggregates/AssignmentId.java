package com.udla.markenx.api.classroom.assignments.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class AssignmentId extends Identifier {
    protected AssignmentId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull AssignmentId generate() {
        return new AssignmentId(java.util.UUID.randomUUID().toString());
    }
}
