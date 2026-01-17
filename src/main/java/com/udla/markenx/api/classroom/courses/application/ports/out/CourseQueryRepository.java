package com.udla.markenx.api.classroom.courses.application.ports.out;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseQueryRepository {
    Page<Course> findAllPaginated(Pageable pageable);
}
