package com.udla.markenx.api.classroom.courses.application.ports.out;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseQueryRepository {
    Optional<Course> findById(String id);
    Course findByIdOrThrow(String id);
    Page<Course> findAllPaginated(Pageable pageable);
}
