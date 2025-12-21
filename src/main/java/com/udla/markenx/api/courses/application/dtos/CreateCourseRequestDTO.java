package com.udla.markenx.api.courses.application.dtos;

public record CreateCourseRequestDTO(
        String name,
        String academicTermId
) {
}
