package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

public record StudentPortDTO(
        String id,
        String label,
        String fullName,
        String email,
        String courseId
) {
}
