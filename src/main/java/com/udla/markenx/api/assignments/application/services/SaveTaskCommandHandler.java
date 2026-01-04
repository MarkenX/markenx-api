package com.udla.markenx.api.assignments.application.services;

import com.udla.markenx.api.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;

public class SaveTaskCommandHandler implements SaveTaskUseCase {
    @Override
    public Task handle(SaveTaskCommand command) {
        return null;
    }
}
