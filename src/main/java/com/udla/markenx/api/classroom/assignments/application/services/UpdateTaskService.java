package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.ports.outgoing.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskCommandRepository repository;

    @Override
    public Task markTaskAsFailedIfOverdue(@NonNull MarkTaskAsFailedIfOverdueCommand command) {
        Task task = repository.findById(command.id());
        task.markAsFailedIfNotCompleted();
        return repository.update(task);
    }

    @Override
    public Task getById(@NonNull GetTaskByIdQuery query) {
        return repository.findById(query.id());
    }
}
