package com.udla.markenx.api.assignments.domain.ports.outgoing;

import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssignmentQueryRepository {
    List<Task> findAll();
    Page<Task> findAllPaginated(Pageable pageable);
}
