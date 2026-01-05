package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

public record StudentResponseDTO(
        String id,
        String label,
        String fullName,
        String email,
        String courseId
) {
}
