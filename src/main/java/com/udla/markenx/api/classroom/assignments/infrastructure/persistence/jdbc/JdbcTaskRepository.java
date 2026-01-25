package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación JDBC del repositorio de comandos para Task.
 * El método save() implementa semántica UPSERT: inserta si no existe, actualiza si existe.
 */
@Repository
@RequiredArgsConstructor
public class JdbcTaskRepository implements TaskCommandRepository {

    private final TaskRowMapper rowMapper = new TaskRowMapper();
    private final JdbcTemplate jdbcTemplate;

    /**
     * Guarda una tarea usando semántica UPSERT.
     * Verifica existencia por ID y decide si insertar o actualizar.
     *
     * @param task la entidad a persistir
     * @return la entidad persistida
     */
    @Override
    @Transactional
    public Task save(@NonNull Task task) {
        if (existsById(task.getId())) {
            update(task);
        } else {
            insert(task);
        }

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM tasks
                WHERE id = ?
                """,
                rowMapper,
                task.getId());
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private void insert(@NonNull Task task) {
        jdbcTemplate.update("""
            INSERT INTO tasks
            (id, lifecycle_status, title, summary, deadline, course_id,
             min_score_to_pass, max_attempts, scenario_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
                task.getId(),
                task.getLifecycleStatus().name(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getCourseId(),
                task.getMinScoreToPass().value(),
                task.getMaxAttempts(),
                task.getScenarioId()
        );
    }

    private void update(@NonNull Task task) {
        jdbcTemplate.update("""
            UPDATE tasks
            SET
                lifecycle_status = ?,
                title = ?,
                summary = ?,
                deadline = ?,
                course_id = ?,
                min_score_to_pass = ?,
                max_attempts = ?,
                scenario_id = ?
            WHERE id = ?
            """,
                task.getLifecycleStatus().name(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getCourseId(),
                task.getMinScoreToPass().value(),
                task.getMaxAttempts(),
                task.getScenarioId(),
                task.getId()
        );
    }
}
