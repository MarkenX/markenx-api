package com.udla.markenx.api.classroom.assignments.application.ports.in.mappers;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import org.jspecify.annotations.NonNull;

public class TaskPortMapper {

    public TaskPortDTO toDTO(@NonNull Task task) {
        return new TaskPortDTO(
                task.getId(),
                task.toString(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getMinScoreToPass().value(),
                task.getStatus().name(),
                task.getMaxAttempts(),
                task.getCourseId(),
                task.getScenarioId()
        );
    }
}
