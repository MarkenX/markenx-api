package com.udla.markenx.api.classroom.terms.application.ports.in.queries;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record TermPageQueryCriteria(int page, int size) {

    @Contract("_, _ -> new")
    public static @NonNull TermPageQueryCriteria from(int page, int size) {
        return new TermPageQueryCriteria(page, size);
    }
}
