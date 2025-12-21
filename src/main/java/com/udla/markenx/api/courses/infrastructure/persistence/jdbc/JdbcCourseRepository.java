package com.udla.markenx.api.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseCommandRepository {

    private final CourseRowMapper rowMapper = new CourseRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Course save(@NonNull Course course) {
        jdbcTemplate.update("""
        INSERT INTO courses
        (id, lifecycle_status, name, academic_term_id)
        VALUES (?, ?, ?, ?)
        """,
            course.getId().value(),
            course.getLifecycleStatus().name(),
            course.getName(),
            course.getAcademicTermId()
        );

        return jdbcTemplate.queryForObject("""
            SELECT *
            FROM courses
            WHERE id = ?
            """,
                rowMapper,
                course.getId().value()
        );
    }
}
