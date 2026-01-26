package com.udla.markenx.api.classroom.courses.application.ports.in.queries;

import com.udla.markenx.api.shared.application.ports.in.queries.FilterMode;

import java.util.Set;

public record CourseStatusQueryCriteria(
        Set<String> statutes,
        FilterMode mode
) {
}
