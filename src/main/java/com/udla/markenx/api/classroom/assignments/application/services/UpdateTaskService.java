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
    private final StudentTaskProgressQueryRepository progressQueryRepository;
    private final StudentTaskProgressCommandRepository progressCommandRepository;
    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO markTaskAsFailedIfOverdue(@NonNull MarkTaskAsFailedIfOverdueCommand command) {
        // This method now operates on all StudentTaskProgress records for the task
        Task task = taskQueryRepository.findByIdOrThrow(command.id());

        // Get all progress records for this task
        var progressRecords = progressQueryRepository.findByTaskId(command.id());

        for (StudentTaskProgress progress : progressRecords) {
            progress.markAsFailedIfOverdue(task.getDeadline().value());
            progressCommandRepository.saveOrUpdate(progress);
        }

        return mapper.toDTO(task);
    }

    @Override
    @Transactional
    public TaskPortDTO registerAttemptResult(@NonNull RegisterTaskAttemptResultCommand command) {
        log.info("Registering attempt result for task: {}, student: {}, score: {}",
                command.taskId(), command.studentId(), command.score());

        // 1. Get task to retrieve configuration
        Task task = taskQueryRepository.findByIdOrThrow(command.taskId());

        // 2. Get or create student task progress
        StudentTaskProgress progress = progressQueryRepository
                .findByStudentIdAndTaskId(command.studentId(), command.taskId())
                .orElseGet(() -> StudentTaskProgress.create(command.studentId(), command.taskId()));

        // 3. Increment attempt counter
        progress.incrementAttempt();

        // 4. Update status based on score and attempt count
        AssignmentScore score = new AssignmentScore(command.score());
        AssignmentScore minScoreToPass = task.getMinScoreToPass();

        progress.registerAttemptResult(
                score,
                minScoreToPass,
                task.getMaxAttempts(),
                task.getDeadline().value()
        );

        // 5. Save/update progress
        progressCommandRepository.saveOrUpdate(progress);

        log.info("Student {} progress updated: currentAttempt={}, status={}",
                command.studentId(), progress.getCurrentAttempt(), progress.getStatus());

        return mapper.toDTO(task);
    }
}
