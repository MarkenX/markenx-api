package com.udla.markenx.api.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.academicterms.application.exceptions.AcademicTermNotFoundException;
import com.udla.markenx.api.courses.application.exceptions.CourseNotFoundException;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseCommandRepository;
import com.udla.markenx.api.students.application.ports.incoming.EnsureCourseExists;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository
        implements CourseCommandRepository, EnsureCourseExists {

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
            throw new CourseNotFoundException(id);
        }
    }

    @Override
    public void ensureExists(String courseId) {
        Boolean exists = jdbcTemplate.queryForObject("""
        SELECT EXISTS (
            SELECT 1
            FROM students
            WHERE id = ?
        )
        """,
                Boolean.class,
                courseId
        );

        if (Boolean.FALSE.equals(exists)) {
            throw new CourseNotFoundException(courseId);
        }
    }
}
