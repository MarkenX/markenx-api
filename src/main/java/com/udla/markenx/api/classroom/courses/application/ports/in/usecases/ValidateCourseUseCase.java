package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.queries.IsActiveCourseQuery;

public interface ValidateCourseUseCase {
    boolean isActive(IsActiveCourseQuery query);
}
