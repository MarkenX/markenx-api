package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressCommandRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementación JDBC del repositorio de comandos para StudentTaskProgress.
 * El método save() implementa semántica UPSERT usando ON DUPLICATE KEY UPDATE,
 * ya que la clave primaria es compuesta (student_id, task_id).
 */
@Repository
@RequiredArgsConstructor
public class JdbcStudentTaskProgressRepository implements StudentTaskProgressCommandRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentTaskProgressRowMapper rowMapper = new StudentTaskProgressRowMapper();

    /**
     * Guarda el progreso usando UPSERT nativo de MySQL.
     * Si el registro (student_id, task_id) no existe, lo inserta.
     * Si ya existe, actualiza current_attempt y status.
     *
     * @param progress el progreso a persistir
     * @return el progreso persistido
     */
    @Override
    public StudentTaskProgress save(@NonNull StudentTaskProgress progress) {
        jdbcTemplate.update("""
            INSERT INTO student_task_progress
            (student_id, task_id, current_attempt, status)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE current_attempt = ?, status = ?
            """,
                progress.getStudentId(),
                progress.getTaskId(),
                progress.getCurrentAttempt(),
                progress.getStatus().name(),
                progress.getCurrentAttempt(),
                progress.getStatus().name()
        );

        return jdbcTemplate.queryForObject("""
            SELECT *
            FROM student_task_progress
            WHERE student_id = ? AND task_id = ?
            """,
                rowMapper,
                progress.getStudentId(),
                progress.getTaskId()
        );
    }
}
