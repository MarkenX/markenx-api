package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentTaskProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentTasksProgressUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * Handler for querying student task progress.
 * Combines data from StudentTaskProgress and Task to provide complete progress info.
 */
@Service
@RequiredArgsConstructor
public class QueryStudentTasksProgressHandler implements QueryStudentTasksProgressUseCase {

    private final StudentTaskProgressQueryRepository progressRepository;
    private final TaskQueryRepository taskRepository;

    @Override
    public StudentTaskProgressPortDTO getProgress(@NonNull StudentTaskProgressQuery query) {
        // Get task to retrieve maxAttempts
        Task task = taskRepository.findByIdOrThrow(query.taskId());

        // Get progress or create default with 0 attempts
        StudentTaskProgress progress = progressRepository
                .findByStudentIdAndTaskId(query.studentId(), query.taskId())
                .orElseGet(() -> StudentTaskProgress.create(query.studentId(), query.taskId()));

        int remainingAttempts = progress.getRemainingAttempts(task.getMaxAttempts());

        return new StudentTaskProgressPortDTO(
                progress.getStudentId(),
                progress.getTaskId(),
                progress.getCurrentAttempt(),
                task.getMaxAttempts(),
                remainingAttempts
        );
    }
}
