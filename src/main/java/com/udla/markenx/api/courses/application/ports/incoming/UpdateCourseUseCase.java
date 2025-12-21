package com.udla.markenx.api.courses.application.ports.incoming;

import com.udla.markenx.api.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;

public interface UpdateCourseUseCase {
    Course getById(GetCourseByIdQuery query);
}
