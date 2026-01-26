package com.udla.markenx.api.classroom.courses.infrastructure.web.dtos;

public record CreateCourseRequestDTO(
        String name,
        String academicTermId
) {
}
