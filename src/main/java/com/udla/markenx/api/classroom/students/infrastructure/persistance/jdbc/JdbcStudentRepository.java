package com.udla.markenx.api.classroom.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación JDBC del repositorio de comandos para Student.
 * El método save() implementa semántica UPSERT: inserta si no existe, actualiza si existe.
 */
@Repository
@RequiredArgsConstructor
public class JdbcStudentRepository implements StudentCommandRepository {

    private final StudentRowMapper rowMapper = new StudentRowMapper();
    private final JdbcTemplate jdbcTemplate;

    /**
     * Guarda un estudiante usando semántica UPSERT.
     * Verifica existencia por ID y decide si insertar o actualizar.
     *
     * @param student la entidad a persistir
     * @return la entidad persistida
     */
    @Override
    @Transactional
    public Student save(@NonNull Student student) {
        if (existsById(student.getId())) {
            update(student);
        } else {
            insert(student);
        }

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM students
                WHERE id = ?
                """,
                rowMapper,
                student.getId());
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private void insert(@NonNull Student student) {
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
    }

    private void update(@NonNull Student student) {
        jdbcTemplate.update("""
            UPDATE students
            SET
                lifecycle_status = ?,
                status = ?,
                first_name = ?,
                last_name = ?,
                course_id = ?,
                user_id = ?
            WHERE id = ?
            """,
                student.getLifecycleStatus().name(),
                student.getStatusCode(),
                student.getFirstName(),
                student.getLastName(),
                student.getCourseId(),
                student.getUserId(),
                student.getId()
        );
    }
}
