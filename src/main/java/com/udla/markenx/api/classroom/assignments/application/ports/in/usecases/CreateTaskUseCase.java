package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;

public interface CreateTaskUseCase {
    TaskPortDTO handle(CreateTaskCommand command);
}
