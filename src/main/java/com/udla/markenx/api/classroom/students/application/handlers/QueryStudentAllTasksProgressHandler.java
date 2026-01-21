package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskWithProgressPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentAllTasksProgressUseCase;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentQueryRepository;
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
public class QueryStudentAllTasksProgressHandler implements QueryStudentAllTasksProgressUseCase {

    private final StudentQueryRepository studentRepository;
    private final TaskQueryRepository taskRepository;
    private final StudentTaskProgressQueryRepository progressRepository;

    @Override
    public List<StudentTaskWithProgressPortDTO> getAllTasksWithProgress(
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

    private StudentTaskWithProgressPortDTO mapToDTO(Task task, StudentTaskProgress progress) {
        int currentAttempt = progress != null ? progress.getCurrentAttempt() : 0;
        int remainingAttempts = task.getMaxAttempts() - currentAttempt;

        return new StudentTaskWithProgressPortDTO(
                task.getId(),
                task.toString(),
                task.getInfo().title(),
                task.getInfo().summary(),
                task.getDeadline().value(),
                task.getMinScoreToPass().value(),
                task.getStatus().name(),
                currentAttempt,
                task.getMaxAttempts(),
                Math.max(0, remainingAttempts)
        );
    }
}
