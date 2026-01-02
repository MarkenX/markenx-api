package com.udla.markenx.api.students.infrastructure.web.rest.dtos;

public record StudentUserReadDTO(
    String studentId,
    String fullName,
    String email
) {
}
