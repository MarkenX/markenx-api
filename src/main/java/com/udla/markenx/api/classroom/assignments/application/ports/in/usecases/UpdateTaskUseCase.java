package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;

public interface UpdateTaskUseCase {
    TaskPortDTO markTaskAsFailedIfOverdue(MarkTaskAsFailedIfOverdueCommand command);
    TaskPortDTO registerAttemptResult(RegisterTaskAttemptResultCommand command);
}
