package com.udla.markenx.api.classroom.assignments.application.ports.in.queries;

import com.udla.markenx.api.shared.application.ports.in.queries.FilterMode;

import java.util.Set;

public record TaskStatusQueryCriteria(
        Set<String> statuses,
        FilterMode mode
) {
}
