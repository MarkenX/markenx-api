package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.mappers;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.infrastructure.web.rest.dtos.TaskResponseDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskResponseDTOMapper {

    public TaskResponseDTO toDTO(@NonNull Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.toString(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getMinScoreToPass().value(),
                task.getStatus().name(),
                task.getMaxAttempts(),
                task.getCurrentAttempt(),
                task.getCourseId()
        );
    }
}
