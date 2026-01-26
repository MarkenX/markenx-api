package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentTaskProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentTasksProgressDetailUseCase;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handler for querying all tasks with student-specific progress.
 * Retrieves tasks from the student's course and combines with progress data.
 */
@Service
@RequiredArgsConstructor
public class QueryStudentTasksProgressDetailHandler implements QueryStudentTasksProgressDetailUseCase {

    private final StudentQueryRepository studentRepository;
    private final TaskQueryRepository taskRepository;
    private final StudentTaskProgressQueryRepository progressRepository;

    @Override
    public List<StudentTaskPortDTO> getAllTasksWithProgress(
            @NonNull StudentAllTasksProgressQuery query
    ) {
        // 1. Get student to retrieve their course
        Student student = studentRepository.findByIdOrThrow(query.studentId());

        // 2. Get all tasks for the student's course
        List<Task> tasks = taskRepository.findByCourseId(student.getCourseId());

        // 3. Get all progress records for this student (batch query for efficiency)
        Map<String, StudentTaskProgress> progressByTaskId = progressRepository
                .findByStudentId(query.studentId())
                .stream()
                .collect(Collectors.toMap(
                        StudentTaskProgress::getTaskId,
                        Function.identity()
                ));

        // 4. Combine task info with progress
        return tasks.stream()
                .map(task -> mapToDTO(task, progressByTaskId.get(task.getId())))
                .toList();
    }

    @Override
    public StudentTaskPortDTO getTaskWithProgress(@NonNull StudentTaskProgressQuery query) {
        // 1. Get task
        Task task = taskRepository.findByIdOrThrow(query.taskId());

        // 2. Get progress or use default (no attempts)
        StudentTaskProgress progress = progressRepository
                .findByStudentIdAndTaskId(query.studentId(), query.taskId())
                .orElse(null);

        return mapToDTO(task, progress);
    }

    private StudentTaskPortDTO mapToDTO(Task task, StudentTaskProgress progress) {
        int currentAttempt = progress != null ? progress.getCurrentAttempt() : 0;
        int remainingAttempts = task.getMaxAttempts() - currentAttempt;
        String status = progress != null
                ? progress.getStatus().name()
                : AssignmentStatus.NOT_STARTED.name();

        return new StudentTaskPortDTO(
                task.getId(),
                task.toString(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getMinScoreToPass().value(),
                status,
                currentAttempt,
                task.getMaxAttempts(),
                Math.max(0, remainingAttempts),
                task.getScenarioId()
        );
    }
}
