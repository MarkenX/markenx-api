package com.udla.markenx.api.students.application.mappers;

import com.udla.markenx.api.students.application.dtos.StudentResponseDTO;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentDTOMapper {

    public StudentResponseDTO toDTO(@NonNull Student domain, String email) {
        return new StudentResponseDTO(
                domain.getId(),
                domain.toString(),
                domain.getFullName(),
                email,
                domain.getCourseId()
        );
    }
}
