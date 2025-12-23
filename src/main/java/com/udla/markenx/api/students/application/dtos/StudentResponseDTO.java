package com.udla.markenx.api.students.application.dtos;

public record StudentResponseDTO(
        String id,
        String label,
        String fullName,
        String email,
        String courseId
) {
}
