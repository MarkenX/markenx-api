package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface TaskCommandRepository {
    Task save(Task task);
    Task update(Task task);
}
