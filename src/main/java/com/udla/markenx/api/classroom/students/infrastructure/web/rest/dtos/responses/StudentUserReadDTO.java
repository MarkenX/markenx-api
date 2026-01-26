package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public record StudentUserReadDTO(
    String id,
    String fullName,
    String email
) {

    @Contract("_ -> new")
    public static @NonNull StudentUserReadDTO from(@NonNull StudentDetailPortDTO dto) {
        return new StudentUserReadDTO(
                dto.studentId(),
                dto.fullName(),
                dto.email()
        );
    }

    public static @NonNull Page<StudentUserReadDTO> from(@NonNull Page<StudentDetailPortDTO> page) {
        return page.map(StudentUserReadDTO::from);
    }
}
