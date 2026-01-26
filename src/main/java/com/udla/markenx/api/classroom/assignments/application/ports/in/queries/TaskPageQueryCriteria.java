package com.udla.markenx.api.classroom.assignments.application.ports.in.queries;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record TaskPageQueryCriteria(int page, int size) {

    @Contract("_, _ -> new")
    public static @NonNull TaskPageQueryCriteria from(int page, int size) {
        return new TaskPageQueryCriteria(page, size);
    }
}
