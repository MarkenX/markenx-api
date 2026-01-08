package com.udla.markenx.api.classroom.courses.domain.ports.outgoing;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseQueryRepository {
    Page<Course> findAllPaginated(Pageable pageable);
}
