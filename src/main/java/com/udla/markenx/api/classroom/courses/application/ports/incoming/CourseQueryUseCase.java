package com.udla.markenx.api.classroom.courses.application.ports.incoming;

import com.udla.markenx.api.classroom.courses.application.queries.GetAllCoursesPaginatedQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;

public interface CourseQueryUseCase {
    Page<Course> getAllPaginated(GetAllCoursesPaginatedQuery query);
}
