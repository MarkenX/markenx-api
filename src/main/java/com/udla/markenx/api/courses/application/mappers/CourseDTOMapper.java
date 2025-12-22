package com.udla.markenx.api.courses.application.mappers;

import com.udla.markenx.api.courses.application.dtos.CourseResponseDTO;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CourseDTOMapper {
    public CourseResponseDTO toDTO(@NonNull Course domain) {
        return new CourseResponseDTO(
                domain.getId().toString(),
                domain.toString(),
                domain.getAcademicTermId()
        );
    }
}
