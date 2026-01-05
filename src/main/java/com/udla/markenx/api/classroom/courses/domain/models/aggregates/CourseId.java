package com.udla.markenx.api.classroom.courses.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class CourseId extends Identifier {
    protected CourseId(String value) {
        super(value);
    }

    @Contract(" -> new")
    public static @NonNull CourseId generate() {
        return new CourseId(java.util.UUID.randomUUID().toString());
    }
}
