package com.udla.markenx.api.students.infrastructure.web.rest.mappers;

import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentResponseDTO;
import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.query.models.StudentSummaryReadModel;
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
