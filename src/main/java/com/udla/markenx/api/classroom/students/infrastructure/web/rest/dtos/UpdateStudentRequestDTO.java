package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

public record UpdateStudentRequestDTO(
        String firstName,
        String lastName,
        String courseId
) {
}
