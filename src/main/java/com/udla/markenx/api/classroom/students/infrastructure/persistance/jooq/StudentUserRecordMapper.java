package com.udla.markenx.api.classroom.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import org.jooq.Record;
import org.jspecify.annotations.NonNull;

public class StudentUserRecordMapper {

    public StudentUserReadDTO toDomain(@NonNull Record r) {
        return new StudentUserReadDTO(
                r.get("id", String.class),
                r.get("user_id", String.class),
                r.get("email", String.class)
        );
    }
}
