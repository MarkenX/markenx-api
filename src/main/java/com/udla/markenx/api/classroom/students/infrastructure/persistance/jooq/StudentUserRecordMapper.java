package com.udla.markenx.api.classroom.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;

public class StudentUserRecordMapper {

    public StudentSummaryReadModel toDomain(@NonNull Record r) {
        return new StudentSummaryReadModel(
                r.get("id", String.class),
                r.get("user_id", String.class),
                r.get("email", String.class)
        );
    }
}
