package com.udla.markenx.api.classroom.assignments.application.ports.in.usecases;

import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskCourseIdQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskPageQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskIdQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryTasksUseCase {
    TaskPortDTO getTaskById(TaskIdQuery query);
    List<TaskPortDTO> listTasks();
    Page<TaskPortDTO> listTasksPage(TaskPageQueryCriteria query);
    List<TaskPortDTO> listTasksByCourseId(TaskCourseIdQueryCriteria criteria);
}
