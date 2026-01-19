package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface UpdateTaskUseCase {
    TaskPortDTO markTaskAsFailedIfOverdue(MarkTaskAsFailedIfOverdueCommand command);
    TaskPortDTO getById(TaskIdQuery query);
    TaskPortDTO registerAttemptResult(RegisterTaskAttemptResultCommand command);
}
