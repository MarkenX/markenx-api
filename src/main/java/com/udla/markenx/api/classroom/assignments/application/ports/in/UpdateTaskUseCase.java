package com.udla.markenx.api.classroom.assignments.application.ports.in;

import com.udla.markenx.api.classroom.assignments.application.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface UpdateTaskUseCase {
    Task markTaskAsFailedIfOverdue(MarkTaskAsFailedIfOverdueCommand command);
    Task getById(GetTaskByIdQuery query);
    Task registerAttemptResult(RegisterTaskAttemptResultCommand command);
}
