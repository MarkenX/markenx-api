package com.udla.markenx.api.classroom.courses.infrastructure.web.dtos;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public record CourseResponseDTO(
        String id,
        String label,
        String termId
) {

    @Contract("_ -> new")
    public static @NonNull CourseResponseDTO from(@NonNull CoursePortDTO course) {
        return new CourseResponseDTO(
                course.id(),
                course.label(),
                course.termId()
        );
    }

    public static @NonNull Page<CourseResponseDTO> from(@NonNull Page<CoursePortDTO> page) {
        return page.map(CourseResponseDTO::from);
    }
}
