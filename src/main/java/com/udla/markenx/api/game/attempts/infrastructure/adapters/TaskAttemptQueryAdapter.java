package com.udla.markenx.api.game.attempts.infrastructure.adapters;

import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskAttemptQueryPort;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter that implements the TaskAttemptQueryPort from the assignments module.
 * This acts as an Anti-Corruption Layer, exposing only the data
 * that the assignments module needs without coupling to internal game types.
 */
@Component
@RequiredArgsConstructor
public class TaskAttemptQueryAdapter implements TaskAttemptQueryPort {

    private final AttemptQueryUseCase attemptQueryUseCase;

    @Override
    public List<TaskAttemptData> findAttemptsByTaskId(String taskId) {
        return attemptQueryUseCase.listAttemptsByTaskId(taskId).stream()
                .map(this::toTaskAttemptData)
                .toList();
    }

    @Override
    public List<TaskAttemptData> findAttemptsByTaskIdAndStudentId(String taskId, String studentId) {
        return attemptQueryUseCase.listAttemptsByTaskIdAndStudentId(taskId, studentId).stream()
                .map(this::toTaskAttemptData)
                .toList();
    }

    private TaskAttemptData toTaskAttemptData(com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO dto) {
        return new TaskAttemptData(
                dto.attemptId(),
                dto.taskId(),
                dto.evaluatedAt(),
                dto.status(),
                dto.finalOutcome(),
                dto.finalAcceptance()
        );
    }
}
