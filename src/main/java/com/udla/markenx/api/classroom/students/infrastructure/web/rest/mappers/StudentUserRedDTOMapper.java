package com.udla.markenx.api.classroom.students.infrastructure.web.rest.mappers;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentUserRedDTOMapper {

    public StudentUserReadDTO toDTO(@NonNull StudentSummaryReadModel model) {
        return new StudentUserReadDTO(
                model.studentId(),
                model.fullName(),
                model.email()
        );
    }
}
