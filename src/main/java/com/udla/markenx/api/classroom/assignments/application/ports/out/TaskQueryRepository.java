package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskQueryRepository {
    Optional<Task> findById(String id);
    Task findByIdOrThrow(String id);
    List<Task> findAll();
    Page<Task> findAllPaginated(Pageable pageable);
    List<Task> findByCourseId(String courseId);
}
