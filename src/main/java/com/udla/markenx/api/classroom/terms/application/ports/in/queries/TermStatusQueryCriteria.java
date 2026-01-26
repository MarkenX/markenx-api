package com.udla.markenx.api.classroom.terms.application.ports.in.queries;

import com.udla.markenx.api.shared.application.ports.in.queries.FilterMode;

import java.util.Set;

public record TermStatusQueryCriteria(
        Set<String> statuses,
        FilterMode mode
) {
}

