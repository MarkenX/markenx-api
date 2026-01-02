package com.udla.markenx.api.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.students.application.exceptions.StudentNotFoundException;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.models.valueobjects.StudentStatus;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcStudentRepository implements StudentCommandRepository {

    private final StudentRowMapper rowMapper = new StudentRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Student save(@NonNull Student student) {
        jdbcTemplate.update("""
            INSERT INTO students
            (id, lifecycle_status, status, first_name, last_name, course_id, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                student.getId(),
                student.getLifecycleStatus().name(),
                student.getStatusCode(),
                student.getFirstName(),
                student.getLastName(),
                student.getCourseId(),
                student.getUserId()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM students
                WHERE id = ?
                """,
                rowMapper,
                student.getId());
    }

    @Override
    public Student findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
            SELECT *
            FROM students
            WHERE id = ?
            """,
                rowMapper,
                id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new StudentNotFoundException(id);
        }
    }

    @Override
    public Student assignUser(String studentId, String userId) {
        int updatedRows = jdbcTemplate.update("""
        UPDATE students
        SET user_id = ?
        WHERE id = ?
        """,
                userId,
                studentId
        );

        if (updatedRows == 0) {
            throw new StudentNotFoundException(studentId);
        }

        return jdbcTemplate.queryForObject("""
        SELECT *
        FROM students
        WHERE id = ?
        """,
                rowMapper,
                studentId
        );
    }

    @Override
    public Student markIdentityFailed(String studentId) {
        int updatedRows = jdbcTemplate.update("""
        UPDATE students
        SET status = ?
        WHERE id = ?
        """,
                StudentStatus.IDENTITY_CREATION_FAILED.name(),
                studentId
        );

        if (updatedRows == 0) {
            throw new StudentNotFoundException(studentId);
        }

        return jdbcTemplate.queryForObject("""
        SELECT *
        FROM students
        WHERE id = ?
        """,
                rowMapper,
                studentId
        );
    }
}
