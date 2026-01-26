package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.responses;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentProfilePortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record StudentProfileResponseDTO(
        String id,
        String email,
        String fullName,
        EnrolledCourseResponseDTO enrolledCourse,
        CurrentTermResponseDTO currentTerm
) {
    public record EnrolledCourseResponseDTO(
            String id,
            String label) {}
    public record CurrentTermResponseDTO(
            String id,
            String label) {}

    @Contract("_ -> new")
    public static @NonNull StudentProfileResponseDTO from(@NonNull StudentProfilePortDTO dto) {
        return new StudentProfileResponseDTO(
                dto.id(),
                dto.email(),
                dto.fullName(),
                new EnrolledCourseResponseDTO(
                        dto.enrolledCourse().id(),
                        dto.enrolledCourse().label()
                ),
                new CurrentTermResponseDTO(
                        dto.currentTerm().id(),
                        dto.currentTerm().label()
                )
        );
    }
}
