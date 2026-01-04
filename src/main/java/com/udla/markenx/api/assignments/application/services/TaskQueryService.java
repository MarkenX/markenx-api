package com.udla.markenx.api.assignments.application.services;

import com.udla.markenx.api.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.ports.outgoing.TaskQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService implements TaskQueryUseCase {

    private final TaskQueryRepository repository;

    @Override
    public List<Task> getAll() {
        return List.of();
    }

    @Override
    public Page<Task> getAllPaginated(GetAllTasksPaginatedQuery query) {
        return null;
    }
}
