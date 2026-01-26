package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record StudentResponseDTO(
        String id,
        String label,
        String fullName,
        String email,
        String courseId,
        String lifecycleStatus
) {

    @Contract("_, _ -> new")
    public static @NonNull StudentResponseDTO from(@NonNull Student student, String email) {
        return new StudentResponseDTO(
                student.getId(),
                student.toString(),
                student.getFullName(),
                email,
                student.getCourseId(),
                student.getLifecycleStatus().name()
        );
    }
}
