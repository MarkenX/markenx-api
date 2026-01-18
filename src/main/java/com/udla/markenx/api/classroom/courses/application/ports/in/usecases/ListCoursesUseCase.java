package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;

public interface ListCoursesUseCase {
    Page<Course> getAllPaginated(CoursePageQueryCriteria query);
}
