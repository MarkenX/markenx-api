package com.udla.markenx.api.classroom.assignments.application.ports.in;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface SaveTaskUseCase {
    Task handle(SaveTaskCommand command);
}
