package com.udla.markenx.api.assignments.application.services;

import com.udla.markenx.api.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public class TaskQueryService implements TaskQueryUseCase {
    @Override
    public List<Task> getAll() {
        return List.of();
    }

    @Override
    public Page<Task> getAllPaginated(GetAllTasksPaginatedQuery query) {
        return null;
    }
}
