package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.classroom.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.classroom.assignments.domain.ports.outgoing.TaskQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService implements TaskQueryUseCase {

    private final TaskQueryRepository repository;

    @Override
    public List<Task> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<Task> getAllPaginated(@NonNull GetAllTasksPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }

    @Override
    public List<Task> getByStatuses(@NonNull List<AssignmentStatus> statuses) {
        return repository.findByStatuses(statuses);
    }
}
