package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.MarkTaskAsFailedIfOverdueCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressCommandRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
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

    private final TaskQueryRepository taskQueryRepository;
    private final TaskCommandRepository taskCommandRepository;
    private final StudentTaskProgressQueryRepository progressQueryRepository;
    private final StudentTaskProgressCommandRepository progressCommandRepository;
    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO markTaskAsFailedIfOverdue(@NonNull MarkTaskAsFailedIfOverdueCommand command) {
        Task task = taskQueryRepository.findByIdOrThrow(command.id());
        task.markAsFailedIfNotCompleted();
        return mapper.toDTO(taskCommandRepository.update(task));
    }

    @Override
    @Transactional
    public TaskPortDTO registerAttemptResult(@NonNull RegisterTaskAttemptResultCommand command) {
        log.info("Registering attempt result for task: {}, student: {}, score: {}",
                command.taskId(), command.studentId(), command.score());

        // 1. Get or create student task progress
        StudentTaskProgress progress = progressQueryRepository
                .findByStudentIdAndTaskId(command.studentId(), command.taskId())
                .orElseGet(() -> StudentTaskProgress.create(command.studentId(), command.taskId()));

        // 2. Increment attempt counter
        progress.incrementAttempt();

        // 3. Save/update progress
        progressCommandRepository.saveOrUpdate(progress);

        log.info("Student {} progress updated: currentAttempt={}",
                command.studentId(), progress.getCurrentAttempt());

        // 4. Get task and update status based on score and current attempt
        Task task = taskQueryRepository.findByIdOrThrow(command.taskId());
        AssignmentScore score = new AssignmentScore(command.score());

        task.registerAttemptResult(score, progress.getCurrentAttempt());

        Task updated = taskCommandRepository.update(task);
        log.info("Task {} updated: status={}", task.getId(), task.getStatus());

        return mapper.toDTO(updated);
    }
}
