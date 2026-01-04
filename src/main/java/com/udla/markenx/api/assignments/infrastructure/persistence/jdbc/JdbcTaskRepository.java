package com.udla.markenx.api.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.ports.outgoing.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTaskRepository implements TaskCommandRepository {
    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Task findById(String id) {
        return null;
    }
}
