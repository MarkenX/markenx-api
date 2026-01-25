package com.udla.markenx.api.game.attempts.infrastructure.adapters;

import com.udla.markenx.api.classroom.students.application.ports.out.StudentAttemptQueryPort;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter that implements the StudentAttemptQueryPort from the students module.
 * This acts as an Anti-Corruption Layer, exposing only the data
 * that the students module needs without coupling to internal game types.
 */
@Component
@RequiredArgsConstructor
public class StudentAttemptQueryAdapter implements StudentAttemptQueryPort {

    private final AttemptQueryUseCase attemptQueryUseCase;

    @Override
    public List<StudentAttemptData> findAttemptsByStudentId(String studentId) {
        return attemptQueryUseCase.listAttemptsByStudentId(studentId).stream()
                .map(dto -> new StudentAttemptData(
                        dto.attemptId(),
                        dto.taskId(),
                        dto.profileDiscoveryPercentage(),
                        dto.finalAcceptance(),
                        dto.remainingBudget(),
                        dto.totalTurnsUsed(),
                        dto.status(),
                        dto.finalOutcome(),
                        dto.evaluatedAt()
                ))
                .toList();
    }
}
