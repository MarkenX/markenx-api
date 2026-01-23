package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.mappers;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskDTOMapper {
    public TaskResponseDTO toResponseDTO(@NonNull TaskPortDTO query) {
        return new TaskResponseDTO(
                query.id(),
                query.label(),
                query.title(),
                query.summary(),
                query.deadline(),
                query.minScoreToPass(),
                query.maxAttempts(),
                query.courseId(),
                query.scenarioId()
        );
    }
}
