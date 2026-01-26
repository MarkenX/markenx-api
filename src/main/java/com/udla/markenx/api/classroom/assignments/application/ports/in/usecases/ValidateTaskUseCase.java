package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.IsTaskOutdatedQuery;

public interface ValidateTaskUseCase {
    boolean isOutdated(IsTaskOutdatedQuery query);
}
