package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskCourseIdQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskStatusQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskPageQueryCriteria;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryTasksService implements QueryTasksUseCase {

    private final TaskQueryRepository repository;
    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO getTaskById(TaskIdQuery query) {
        return null;
    }

    @Override
    public List<TaskPortDTO> listTasks() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public Page<TaskPortDTO> listTasksPage(@NonNull TaskPageQueryCriteria query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable).map(mapper::toDTO);
    }

    @Override
    public List<TaskPortDTO> listTasksByStatuses(@NonNull TaskStatusQueryCriteria criteria) {
        return repository.findByStatuses(criteria.statuses()).stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<TaskPortDTO> listTasksByCourseId(TaskCourseIdQueryCriteria criteria) {
        return repository.findByCourseId(criteria.courseId()).stream().map(mapper::toDTO).toList();
    }
}
