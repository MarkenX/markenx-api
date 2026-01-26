package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public record StudentResponseDTO(
        String id,
        String label,
        String fullName,
        String email,
        String courseId,
        String lifecycleStatus
) {

    @Contract("_, _ -> new")
    public static @NonNull StudentResponseDTO from(@NonNull Student student, @NonNull String email) {
        return new StudentResponseDTO(
                student.getId(),
                student.toString(),
                student.getFullName(),
                email,
                student.getCourseId(),
                student.getLifecycleStatus().name()
        );
    }

    @Contract("_ -> new")
    public static @NonNull StudentResponseDTO from(@NonNull StudentDetailPortDTO dto) {
        return new StudentResponseDTO(
                dto.studentId(),
                dto.label(),
                dto.fullName(),
                dto.email(),
                dto.courseId(),
                dto.lifecycleStatus()
        );
    }

    public static @NonNull Page<StudentResponseDTO> from(@NonNull Page<StudentDetailPortDTO> page) {
        return page.map(StudentResponseDTO::from);
    }
}
