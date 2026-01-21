package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * jOOQ implementation of StudentTaskProgressQueryRepository.
 */
@Repository
@RequiredArgsConstructor
public class JooqStudentTaskProgressRepository implements StudentTaskProgressQueryRepository {

    private static final String TABLE = "student_task_progress";

    private final DSLContext dsl;

    @Override
    public Optional<StudentTaskProgress> findByStudentIdAndTaskId(
            @NonNull String studentId,
            @NonNull String taskId
    ) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("student_id").eq(studentId))
                .and(field("task_id").eq(taskId))
                .fetchOptional()
                .map(this::mapToProgress);
    }

    @Override
    public List<StudentTaskProgress> findByStudentId(@NonNull String studentId) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("student_id").eq(studentId))
                .fetch()
                .map(this::mapToProgress);
    }

    @Override
    public List<StudentTaskProgress> findByTaskId(@NonNull String taskId) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("task_id").eq(taskId))
                .fetch()
                .map(this::mapToProgress);
    }

    private StudentTaskProgress mapToProgress(Record record) {
        return new StudentTaskProgress(
                record.get("student_id", String.class),
                record.get("task_id", String.class),
                record.get("current_attempt", Integer.class)
        );
    }
}
