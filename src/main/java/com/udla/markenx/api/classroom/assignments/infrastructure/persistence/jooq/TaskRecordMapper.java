package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

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
                AssignmentStatus.valueOf(r.get("status", String.class)),
                r.get("course_id", String.class),
                r.get("max_attempts", Integer.class),
                r.get("current_attempt", Integer.class)
        );
    }
}