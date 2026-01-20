package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
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

    private final TaskQueryRepository queryRepository;
    private final TaskCommandRepository commandRepository;
    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO markTaskAsFailedIfOverdue(@NonNull MarkTaskAsFailedIfOverdueCommand command) {
        Task task = queryRepository.findByIdOrThrow(command.id());
        task.markAsFailedIfNotCompleted();
        return mapper.toDTO(commandRepository.update(task));
    }

    @Override
    @Transactional
    public TaskPortDTO registerAttemptResult(@NonNull RegisterTaskAttemptResultCommand command) {
        log.info("Registering attempt result for task: {}, score: {}", command.taskId(), command.score());

        Task task = queryRepository.findByIdOrThrow(command.taskId());
        AssignmentScore score = new AssignmentScore(command.score());

        task.registerAttemptResult(score);

        Task updated = commandRepository.update(task);
        log.info("Task {} updated: status={}, currentAttempt={}",
                task.getId(), task.getStatus(), task.getCurrentAttempt());

        return mapper.toDTO(updated);
    }
}
