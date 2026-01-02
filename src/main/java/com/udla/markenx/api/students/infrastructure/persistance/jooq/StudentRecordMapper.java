package com.udla.markenx.api.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.models.valueobjects.StudentStatus;
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
            r.get("course_id", String.class),
            r.get("user_id", String.class),
            StudentStatus.valueOf(r.get("status", String.class))
        );
    }
}
