package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.ChangeTaskStatusCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.UpdateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;

public interface UpdateTaskUseCase {
    TaskPortDTO update(UpdateTaskCommand command);
    TaskPortDTO changeStatus(ChangeTaskStatusCommand command);
    void registerAttemptResult(RegisterTaskAttemptResultCommand command);
}
