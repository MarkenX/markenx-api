package com.udla.markenx.api.classroom.assignments.domain.models.aggregates;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidMaxAttemptsException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidScenarioIdException;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * Task aggregate representing an assignment that students can attempt.
 * Note: status and currentAttempt are tracked per student in StudentTaskProgress.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public class Task extends Assignment {

    private int maxAttempts;
    private String scenarioId;

    // region Constructors

    private Task(
            AssignmentId id,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentScore minScoreToPass,
            String courseId,
            int maxAttempts,
            String scenarioId
    ) {
        super(id, info, deadline, minScoreToPass, courseId);
        this.maxAttempts = validateMaxAttempts(maxAttempts);
        this.scenarioId = validateScenarioId(scenarioId);
    }

    public Task(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            String title,
            String summary,
            LocalDateTime deadline,
            double minScoreToPass,
            String courseId,
            int maxAttempts,
            String scenarioId
    ) {
        super(id, lifecycleStatus, code, title, summary, deadline, minScoreToPass, courseId);
        this.maxAttempts = validateMaxAttempts(maxAttempts);
        this.scenarioId = validateScenarioId(scenarioId);
    }

    // endregion

    // region Factories

    public static @NonNull Task create(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String courseId,
            int maxAttempts,
            String scenarioId
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.future(deadline),
                minScoreToPass,
                courseId,
                maxAttempts,
                scenarioId
        );
    }

    public static @NonNull Task createHistorical(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String courseId,
            int maxAttempts,
            String scenarioId
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.historical(deadline),
                minScoreToPass,
                courseId,
                maxAttempts,
                scenarioId
        );
    }

    // endregion

    // region Getters

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public String getScenarioId() {
        return this.scenarioId;
    }

    // endregion

    // region Setters

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = validateMaxAttempts(maxAttempts);
    }

    // endregion

    // region Validations

    public int validateMaxAttempts(int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new InvalidMaxAttemptsException();
        }
        return maxAttempts;
    }

    public String validateScenarioId(String scenarioId) {
        if (scenarioId == null || scenarioId.isBlank()) {
            throw new InvalidScenarioIdException();
        }
        return scenarioId;
    }

    // endregion

    @Override
    public String toString() {
        return String.format("TSK-%s", formatCode());
    }
}
