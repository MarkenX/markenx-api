package com.udla.markenx.api.classroom.assignments.application.ports.in.queries;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record TaskIdQuery(String id) {

    @Contract("_ -> new")
    public static @NonNull TaskIdQuery from(String id) {
        return new TaskIdQuery(id);
    }
}
