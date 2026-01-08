package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.ports.outgoing.TaskCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTaskRepository implements TaskCommandRepository {

    private final TaskRowMapper rowMapper = new TaskRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Task save(@NonNull Task task) {
        jdbcTemplate.update("""
        INSERT INTO tasks
        (id, lifecycle_status, status, title, summary, deadline, course_id, 
         min_score_to_pass, max_attempts, current_attempt)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """,
                task.getId(),
                task.getLifecycleStatus().name(),
                task.getStatus().name(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getCourseId(),
                task.getMinScoreToPass().value(),
                task.getMaxAttempts(),
                task.getCurrentAttempt()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM tasks
                WHERE id = ?
                """,
                rowMapper,
                task.getId());
    }

    @Override
    public Task findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
            SELECT *
            FROM tasks
            WHERE id = ?
            """,
                rowMapper,
                id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                "No se encontró la tarea con el identificador:" + id
            );
        }
    }

    @Override
    public Task update(@NonNull Task task) {
        int updatedRows = jdbcTemplate.update("""
        UPDATE tasks
        SET
            lifecycle_status = ?,
            status = ?,
            title = ?,
            summary = ?,
            deadline = ?,
            course_id = ?,
            min_score_to_pass = ?,
            max_attempts = ?,
            current_attempt = ?
        WHERE id = ?
        """,
                task.getLifecycleStatus().name(),
                task.getStatus().name(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getCourseId(),
                task.getMinScoreToPass().value(),
                task.getMaxAttempts(),
                task.getCurrentAttempt(),
                task.getId()
        );

        if (updatedRows == 0) {
            throw new EntityNotFoundException(
                "No se encontró la tarea con el identificador:" + task.getId()
            );
        }

        return jdbcTemplate.queryForObject("""
            SELECT *
            FROM tasks
            WHERE id = ?
            """,
                rowMapper,
                task.getId());
    }
}
