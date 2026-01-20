package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

public record StudentProfilePortDTO(
        String id,
        String email,
        String fullName,
        String enrolledCourse,
        String activeAcademicTerm
) {}

