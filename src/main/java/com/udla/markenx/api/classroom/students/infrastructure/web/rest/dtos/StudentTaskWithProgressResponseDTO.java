package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressDetailPortDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for a task with student-specific progress.
 *
 * @param id The ID of the task
 * @param label The task label (e.g., "TSK-0001")
 * @param title The task title
 * @param summary The task summary
 * @param deadline The task deadline
 * @param minScoreToPass The minimum score required to pass (0.0-1.0)
 * @param status The task progress status
 * @param currentAttempt The student's current attempt number (0 if no attempts yet)
 * @param maxAttempts The maximum attempts allowed for the task
 * @param remainingAttempts The number of remaining attempts for the student
 * @param scenarioId The ID of the scenario associated with the task
 */
public record StudentTaskWithProgressResponseDTO(
        String id,
        String label,
        String title,
        String summary,
        LocalDateTime deadline,
        double minScoreToPass,
        TaskProgressStatus status,
        int currentAttempt,
        int maxAttempts,
        int remainingAttempts,
        String scenarioId
) {

    @Contract("_ -> new")
    public static @NonNull StudentTaskWithProgressResponseDTO from(@NonNull StudentTaskProgressDetailPortDTO dto) {
        return new StudentTaskWithProgressResponseDTO(
                dto.taskId(),
                dto.taskLabel(),
                dto.title(),
                dto.summary(),
                dto.deadline(),
                dto.minScoreToPass(),
                TaskProgressStatus.fromName(dto.status()),
                dto.currentAttempt(),
                dto.maxAttempts(),
                dto.remainingAttempts(),
                dto.scenarioId()
        );
    }

    public static @NonNull List<StudentTaskWithProgressResponseDTO> from(@NonNull List<StudentTaskProgressDetailPortDTO> list) {
        return list.stream().map(StudentTaskWithProgressResponseDTO::from).toList();
    }
}
