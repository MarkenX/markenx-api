package com.udla.markenx.api.classroom.students.infrastructure.web.rest.mappers;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.classroom.students.application.dtos.StudentDetailPortDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentUserReadDTOMapper {

    public StudentUserReadDTO toDTO(@NonNull StudentDetailPortDTO model) {
        return new StudentUserReadDTO(
                model.studentId(),
                model.fullName(),
                model.email()
        );
    }
}
