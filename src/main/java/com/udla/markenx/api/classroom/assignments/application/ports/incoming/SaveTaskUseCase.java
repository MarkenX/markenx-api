package com.udla.markenx.api.classroom.assignments.application.ports.incoming;

import com.udla.markenx.api.classroom.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;

public interface SaveTaskUseCase {
    Task handle(SaveTaskCommand command);
}
