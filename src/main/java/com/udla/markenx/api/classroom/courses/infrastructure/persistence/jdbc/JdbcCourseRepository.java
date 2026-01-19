package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.application.ports.incoming.EnsureCourseHasUpcomingTermForAssignment;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseCommandRepository,
        EnsureCourseHasUpcomingTermForAssignment {

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
            course.getTermId()
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

    @Override
    public Course findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
            SELECT *
            FROM courses
            WHERE id = ?
            """,
                rowMapper,
                id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Curso", id);
        }
    }

    @Override
    public void ensureCourseHasUpcomingTerm(String courseId) {

    }
}
