package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

public record CreateStudentRequestDTO(
        String firstName,
        String lastName,
        String email,
        String courseId
) {
}
