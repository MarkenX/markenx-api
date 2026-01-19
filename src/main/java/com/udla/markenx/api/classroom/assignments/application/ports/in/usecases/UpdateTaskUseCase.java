package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface UpdateTaskUseCase {
    Task markTaskAsFailedIfOverdue(MarkTaskAsFailedIfOverdueCommand command);
    Task getById(TaskIdQuery query);
    Task registerAttemptResult(RegisterTaskAttemptResultCommand command);
}
