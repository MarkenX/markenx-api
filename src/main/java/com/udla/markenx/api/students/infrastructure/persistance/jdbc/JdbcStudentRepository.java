package com.udla.markenx.api.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.students.application.exceptions.StudentNotFoundException;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
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
            (id, lifecycle_status, first_name, last_name, course_id, user_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
                student.getId(),
                student.getLifecycleStatus().name(),
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
}
