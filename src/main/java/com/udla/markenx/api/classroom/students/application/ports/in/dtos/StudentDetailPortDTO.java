package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

public record StudentDetailPortDTO(
        String studentId,
        String email,
        String fullName
) {}
