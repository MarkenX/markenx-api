package com.udla.markenx.api.classroom.courses.application.dtos;

public record CreateCourseRequestDTO(
        String name,
        String academicTermId
) {
}
