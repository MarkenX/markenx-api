package com.udla.markenx.api.classroom.assignments.infrastructure.tasks;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.TaskStatusQueryCriteria;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.application.ports.in.queries.FilterMode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TaskStatusScheduler {

    private static final Set<String> PROCESSABLE_STATUSES = Set.of(
            AssignmentStatus.NOT_STARTED.name(),
            AssignmentStatus.IN_PROGRESS.name()
    );

    private final QueryTasksUseCase queryTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndUpdateTaskStatuses() {
        TaskStatusQueryCriteria criteria = new TaskStatusQueryCriteria(PROCESSABLE_STATUSES, FilterMode.INCLUDE);
        List<TaskPortDTO> tasks = queryTasksUseCase.listTasksByStatuses(criteria);
        for (TaskPortDTO task : tasks) {
            var command = new MarkTaskAsFailedIfOverdueCommand(task.id());
            updateTaskUseCase.markTaskAsFailedIfOverdue(command);
        }
    }

}
