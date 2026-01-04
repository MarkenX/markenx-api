package com.udla.markenx.api.assignments.domain.ports.outgoing;

import com.udla.markenx.api.assignments.domain.models.aggregates.Task;

public interface TaskCommandRepository {
    Task save(Task task);
    Task findById(String id);
}
