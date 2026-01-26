package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Course save(@NonNull Course course) {
        jdbcTemplate.update("""
        INSERT INTO courses
        (id, lifecycle_status, name, academic_term_id)
        VALUES (?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            lifecycle_status  = VALUES(lifecycle_status),
            name              = VALUES(name),
            academic_term_id  = VALUES(academic_term_id)
        """,
                course.getId().value(),
                course.getLifecycleStatus().name(),
                course.getName(),
                course.getTermId()
        );

        return course;
    }
}
