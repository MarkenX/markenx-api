package com.udla.markenx.api.classroom.terms.application.ports.in.queries;

import java.util.Set;

public record TermStatusQueryCriteria(
        Set<String> statuses,
        FilterMode mode
) {
}

