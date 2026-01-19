package com.udla.markenx.api.classroom.assignments.application.ports.in.queries;

import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;

import java.util.List;

public record TaskStatusQueryCriteria(
        List<AssignmentStatus> statuses
) {
}
