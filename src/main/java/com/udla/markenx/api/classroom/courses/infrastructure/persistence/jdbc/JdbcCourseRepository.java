package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.EnsureCourseHasUpcomingTermForAssignment;
import com.udla.markenx.api.classroom.courses.application.exceptions.CourseNotFoundException;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.domain.ports.outgoing.CourseCommandRepository;
import com.udla.markenx.api.classroom.students.application.ports.incoming.CourseValidation;
import com.udla.markenx.api.classroom.students.application.ports.incoming.EnsureCourseHasUpcomingTerm;
import com.udla.markenx.api.classroom.students.domain.exceptions.CourseNotInUpcomingTermException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCourseRepository
        implements CourseCommandRepository, CourseValidation,
                   EnsureCourseHasUpcomingTerm, EnsureCourseHasUpcomingTermForAssignment {

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
    public void ensureCourseExists(String courseId) {
        Boolean exists = jdbcTemplate.queryForObject("""
        SELECT EXISTS (
            SELECT 1
            FROM courses
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

    @Override
    public void ensureCourseHasUpcomingTerm(String courseId) {
        ensureCourseExists(courseId);

        String status = jdbcTemplate.queryForObject("""
            SELECT at.status
            FROM courses c
            JOIN academic_terms at ON c.academic_term_id = at.id
            WHERE c.id = ?
            """,
                String.class,
                courseId
        );

        if (!AcademicTermStatus.UPCOMING.name().equals(status)) {
            throw new CourseNotInUpcomingTermException(courseId);
        }
    }
}
