package com.udla.markenx.api.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.ports.outgoing.TaskQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JooqTaskRepository implements TaskQueryRepository {
    @Override
    public List<Task> findAll() {
        return List.of();
    }

    @Override
    public Page<Task> findAllPaginated(Pageable pageable) {
        return null;
    }
}
