package com.udla.markenx.api.classroom.courses.application.ports.in.mappers;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import org.jspecify.annotations.NonNull;

public class CoursePortMapper {
    public CoursePortDTO toDTO(@NonNull Course domain) {
        return new CoursePortDTO(
                domain.getId().toString(),
                domain.toString(),
                domain.getTermId(),
                domain.getLifecycleStatus().name()
        );
    }
}
