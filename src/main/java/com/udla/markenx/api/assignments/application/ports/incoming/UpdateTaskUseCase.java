package com.udla.markenx.api.assignments.application.ports.incoming;

import com.udla.markenx.api.assignments.application.commands.UpdateTaskStatusCommand;
import com.udla.markenx.api.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;

public interface UpdateTaskUseCase {
    Task updateStatus(UpdateTaskStatusCommand command);
    Task getById(GetTaskByIdQuery query);
}
