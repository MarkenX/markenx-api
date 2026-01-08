package com.udla.markenx.api.classroom.assignments.domain.ports.outgoing;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskQueryRepository {
    List<Task> findAll();
    Page<Task> findAllPaginated(Pageable pageable);
    List<Task> findByStatuses(List<AssignmentStatus> statuses);
}
