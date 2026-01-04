package com.udla.markenx.api.assignments.application.ports.incoming;

import com.udla.markenx.api.assignments.application.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;

public interface UpdateTaskUseCase {
    Task markTaskAsFailedIfOverdue(MarkTaskAsFailedIfOverdueCommand command);
    Task getById(GetTaskByIdQuery query);
}
