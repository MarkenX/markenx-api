package com.udla.markenx.api.assignments.application.ports.incoming;

import com.udla.markenx.api.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;

public interface SaveTaskUseCase {
    Task handle(SaveTaskCommand command);
}
