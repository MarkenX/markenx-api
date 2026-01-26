package com.udla.markenx.api.classroom.terms.application.ports.in.queries;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record TermIdQuery(String id) {

    @Contract("_ -> new")
    public static @NonNull TermIdQuery from(String id) {
        return new TermIdQuery(id);
    }
}
