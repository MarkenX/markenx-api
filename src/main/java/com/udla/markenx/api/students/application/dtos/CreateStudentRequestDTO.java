package com.udla.markenx.api.students.application.dtos;

public record CreateStudentRequestDTO(
        String firstName,
        String lastName,
        String email,
        String courseId
) {
}
