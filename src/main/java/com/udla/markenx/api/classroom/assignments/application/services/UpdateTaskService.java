package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.queries.GetTaskByIdQuery;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskCommandRepository repository;

    @Override
    public Task markTaskAsFailedIfOverdue(@NonNull MarkTaskAsFailedIfOverdueCommand command) {
        Task task = repository.findById(command.id());
        task.markAsFailedIfNotCompleted();
        return repository.update(task);
    }

    @Override
    public Task getById(@NonNull GetTaskByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    @Transactional
    public Task registerAttemptResult(@NonNull RegisterTaskAttemptResultCommand command) {
        log.info("Registering attempt result for task: {}, score: {}", command.taskId(), command.score());

        Task task = repository.findById(command.taskId());
        AssignmentScore score = new AssignmentScore(command.score());

        task.registerAttemptResult(score);

        Task updated = repository.update(task);
        log.info("Task {} updated: status={}, currentAttempt={}",
                task.getId(), task.getStatus(), task.getCurrentAttempt());

        return updated;
    }
}
