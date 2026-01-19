package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface CreateTaskUseCase {
    Task handle(SaveTaskCommand command);
}
