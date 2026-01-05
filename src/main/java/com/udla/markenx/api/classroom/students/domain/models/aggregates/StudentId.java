package com.udla.markenx.api.classroom.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class StudentId extends Identifier {
    protected StudentId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull StudentId generate() {
        return new StudentId(java.util.UUID.randomUUID().toString());
    }
}
