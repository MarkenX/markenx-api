package com.udla.markenx.api.classroom.assignments.infrastructure.tasks;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.QueryTasksUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskStatusScheduler {

    private static final List<AssignmentStatus> PROCESSABLE_STATUSES = List.of(
            AssignmentStatus.NOT_STARTED,
            AssignmentStatus.IN_PROGRESS
    );

    private final QueryTasksUseCase queryTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndUpdateTaskStatuses() {
        List<Task> tasks = queryTasksUseCase.listTasksByStatuses(PROCESSABLE_STATUSES);
        for (Task task : tasks) {
            var command = new MarkTaskAsFailedIfOverdueCommand(task.getId());
            updateTaskUseCase.markTaskAsFailedIfOverdue(command);
        }
    }

}
