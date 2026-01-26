package com.udla.markenx.api.classroom.courses.application.ports.in.dtos;

public record CoursePortDTO(
        String id,
        String code,
        String label,
        String termId,
        String lifecycleStatus
) {
}
