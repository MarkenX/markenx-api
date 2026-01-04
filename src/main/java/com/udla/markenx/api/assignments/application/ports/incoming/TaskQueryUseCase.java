package com.udla.markenx.api.assignments.application.ports.incoming;

import com.udla.markenx.api.assignments.application.queries.GetAllTasksPaginatedQuery;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import org.springframework.data.domain.Page;

public interface TaskQueryUseCase {
    Page<Task> getAllPaginated(GetAllTasksPaginatedQuery query);
}
