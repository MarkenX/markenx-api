package com.udla.markenx.api.classroom.students.application.ports.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Anti-Corruption Layer port for querying attempt data from the game module.
 * This port decouples the students module from the game/attempts module,
 * following DDD bounded context principles.
 */
public interface StudentAttemptQueryPort {

    /**
     * Finds all attempts associated with a student.
     *
     * @param studentId the student ID
     * @return list of attempt data for the student
     */
    List<StudentAttemptData> findAttemptsByStudentId(String studentId);

    /**
     * Data transfer record containing attempt information relevant to students.
     */
    record StudentAttemptData(
            String attemptId,
            String taskId,
            double profileDiscoveryPercentage,
            double finalAcceptance,
            BigDecimal remainingBudget,
            int totalTurnsUsed,
            String status,
            String finalOutcome,
            LocalDateTime evaluatedAt
    ) {}
}
