package com.udla.markenx.api.classroom.assignments.application.ports.out;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Anti-Corruption Layer port for querying attempt data from the game module.
 * This port decouples the assignments module from the game/attempts module,
 * following DDD bounded context principles.
 */
public interface TaskAttemptQueryPort {

    /**
     * Finds all attempts associated with a task.
     *
     * @param taskId the task ID
     * @return list of attempt data for the task
     */
    List<TaskAttemptData> findAttemptsByTaskId(String taskId);

    /**
     * Data transfer record containing attempt information relevant to tasks.
     */
    record TaskAttemptData(
            String attemptId,
            String taskId,
            LocalDateTime evaluatedAt,
            String finalOutcome,
            double finalAcceptance
    ) {}
}
