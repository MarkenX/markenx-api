package com.udla.markenx.api.courses.domain.ports.outgoing;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepository {
    Course save(Course course);
    Page<Course> findAllPaginated(Pageable pageable);
}
