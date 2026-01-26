package com.udla.markenx.api.classroom.students.application.ports.in.queries;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record StudentIdQuery(String id) {

    @Contract("_ -> new")
    public static @NonNull StudentIdQuery from(String id) {
        return new StudentIdQuery(id);
    }
}
