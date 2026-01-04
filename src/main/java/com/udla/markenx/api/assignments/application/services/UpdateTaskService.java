package com.udla.markenx.api.assignments.application.services;

import com.udla.markenx.api.assignments.application.commands.UpdateTaskStatusCommand;
import com.udla.markenx.api.assignments.application.ports.incoming.UpdateTaskUseCase;
import com.udla.markenx.api.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.ports.outgoing.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskCommandRepository repository;

    @Override
    public Task updateStatus(@NonNull UpdateTaskStatusCommand command) {
        Task task = repository.findById(command.id());
        task.markAsFailedIfNotCompleted();
        return repository.save(task);
    }

    @Override
    public Task getById(@NonNull GetTaskByIdQuery query) {
        return repository.findById(query.id());
    }
}
