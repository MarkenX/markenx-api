package com.udla.markenx.api.students.application.mappers;

import com.udla.markenx.api.students.application.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentUserReadDTOMapper {

    public StudentUserReadDTO toDTO(@NonNull Student domain, String email) {
        return new StudentUserReadDTO(
                domain.getId(),
                domain.getUserId(),
                email
        );
    }
}
