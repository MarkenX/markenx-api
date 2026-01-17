package com.udla.markenx.api.classroom.academicterms.application.ports.in.queries;

import java.util.Set;

public record TermStatusQueryCriteria(
        Set<String> statuses,
        FilterMode mode
) {
}

