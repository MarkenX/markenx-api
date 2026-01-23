package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressCommandRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of StudentTaskProgressCommandRepository.
 */
@Repository
@RequiredArgsConstructor
public class JdbcStudentTaskProgressRepository implements StudentTaskProgressCommandRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentTaskProgressRowMapper rowMapper = new StudentTaskProgressRowMapper();

    @Override
    public StudentTaskProgress save(@NonNull StudentTaskProgress progress) {
        jdbcTemplate.update("""
            INSERT INTO student_task_progress
            (student_id, task_id, current_attempt, status)
            VALUES (?, ?, ?, ?)
            """,
                progress.getStudentId(),
                progress.getTaskId(),
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

    @Override
    public StudentTaskProgress update(@NonNull StudentTaskProgress progress) {
        jdbcTemplate.update("""
            UPDATE student_task_progress
            SET current_attempt = ?, status = ?
            WHERE student_id = ? AND task_id = ?
            """,
                progress.getCurrentAttempt(),
                progress.getStatus().name(),
                progress.getStudentId(),
                progress.getTaskId()
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

    @Override
    public StudentTaskProgress saveOrUpdate(@NonNull StudentTaskProgress progress) {
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
