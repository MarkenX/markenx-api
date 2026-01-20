package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.IsTaskOutdatedQuery;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.ValidateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTaskService implements ValidateTaskUseCase {

    private final TaskQueryRepository repository;

    @Override
    public boolean isOutdated(@NonNull IsTaskOutdatedQuery query) {
        Task task = repository.findByIdOrThrow(query.taskId());
        return task.isOutdated();
    }
}
