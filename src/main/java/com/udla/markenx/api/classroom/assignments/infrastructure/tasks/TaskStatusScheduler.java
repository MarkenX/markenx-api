package com.udla.markenx.api.classroom.assignments.infrastructure.tasks;

import com.udla.markenx.api.classroom.assignments.application.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskStatusScheduler {

    private final TaskQueryUseCase taskQueryUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndUpdateTaskStatuses() {
        List<Task> tasks = taskQueryUseCase.getAll();
        for (Task task : tasks) {
            var command = new MarkTaskAsFailedIfOverdueCommand(task.getId());
            updateTaskUseCase.markTaskAsFailedIfOverdue(command);
        }
    }

}
