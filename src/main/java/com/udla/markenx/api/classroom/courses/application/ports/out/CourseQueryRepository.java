package com.udla.markenx.api.classroom.courses.application.ports.out;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseQueryRepository {
    Optional<Course> findById(String id);
    Course findByIdOrThrow(String id);
    List<Course> findAll();
    List<Course> findAllByStatus(Set<String> statuses, boolean exclude);
    Page<Course> findAllPaginated(Pageable pageable);
}
