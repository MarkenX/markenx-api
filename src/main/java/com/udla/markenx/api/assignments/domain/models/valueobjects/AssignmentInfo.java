package com.udla.markenx.api.assignments.domain.models.valueobjects;

import com.udla.markenx.api.assignments.domain.exceptions.InvalidAssignmentDescriptionException;
import com.udla.markenx.api.assignments.domain.exceptions.InvalidAssignmentTitleException;

public record AssignmentInfo(
        String title,
        String summary
) {

    public AssignmentInfo {
        if (title == null || title.isBlank())
            throw new InvalidAssignmentTitleException();

        if (summary == null || summary.isBlank())
            throw new InvalidAssignmentDescriptionException();
    }
}


