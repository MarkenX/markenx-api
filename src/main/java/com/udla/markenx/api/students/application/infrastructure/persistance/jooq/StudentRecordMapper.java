package com.udla.markenx.api.students.application.infrastructure.persistance.jooq;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;

public class StudentRecordMapper {

    public Student toDomain(@NonNull Record r) {
        return new Student(
                r.get("id", String.class),
                LifecycleStatus.valueOf(r.get("lifecycle_status", String.class)),
                r.get("code", Long.class),
                r.get("first_name", String.class),
                r.get("last_name", String.class),
                r.get("email", String.class),
                r.get("course_id", String.class)
        );
    }
}
