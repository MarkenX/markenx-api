package com.udla.markenx.api.assignments.application.ports.incoming;

import com.udla.markenx.api.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskQueryUseCase {
    List<Task> getAll();
    Page<Task> getAllPaginated(GetAllTasksPaginatedQuery query);
}
