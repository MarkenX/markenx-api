package com.udla.markenx.api.students.application.dtos;

public record StudentUserReadDTO(
    String studentId,
    String userId,
    String email
) {
}
