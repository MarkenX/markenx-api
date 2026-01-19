package com.udla.markenx.api.classroom.courses.infrastructure.web.mappers;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.infrastructure.web.dtos.CourseResponseDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CourseDTOMapper {
    public CourseResponseDTO toDTO(@NonNull CoursePortDTO query) {
        return new CourseResponseDTO(
                query.id(),
                query.label(),
                query.termId()
        );
    }
}

