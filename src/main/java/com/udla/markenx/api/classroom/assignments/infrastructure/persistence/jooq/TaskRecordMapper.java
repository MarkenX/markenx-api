package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * Maps jOOQ records to Task domain objects.
 * Note: status and currentAttempt are now tracked in student_task_progress table.
 */
public class TaskRecordMapper {

    public Task toDomain(@NonNull Record r) {
        return new Task(
                r.get("id", String.class),
                LifecycleStatus.valueOf(r.get("lifecycle_status", String.class)),
                r.get("code", Long.class),
                r.get("title", String.class),
                r.get("summary", String.class),
                r.get("deadline", LocalDateTime.class),
                r.get("min_score_to_pass", Double.class),
                r.get("course_id", String.class),
                r.get("max_attempts", Integer.class),
                r.get("scenario_id", String.class)
        );
    }
}
