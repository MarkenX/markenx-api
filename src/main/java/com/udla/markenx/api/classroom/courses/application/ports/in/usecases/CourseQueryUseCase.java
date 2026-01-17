package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.queries.GetAllCoursesPaginatedQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;

public interface CourseQueryUseCase {
    Page<Course> getAllPaginated(GetAllCoursesPaginatedQuery query);
}
