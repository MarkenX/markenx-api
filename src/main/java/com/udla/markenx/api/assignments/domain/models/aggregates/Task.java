package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.exceptions.InvalidCurrentAttemptException;
import com.udla.markenx.api.assignments.domain.exceptions.InvalidMaxAttemptsException;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public class Task extends Assignment {

    private int maxAttempts;
    private int currentAttempt;

    // region Constructors

    private Task(
            AssignmentId id,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentScore minScoreToPass,
            AssignmentStatus status,
            String academicTermId,
            int maxAttempts,
            int currentAttempt
    ) {
        super(id, info, deadline, minScoreToPass, status, academicTermId);
        this.maxAttempts = validateMaxAttempts(maxAttempts);
        this.currentAttempt = validateCurrentAttempt(currentAttempt);
    }

    public Task(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            String title,
            String summary,
            LocalDateTime deadline,
            double minScoreToPass,
            AssignmentStatus status,
            String courseId,
            int maxAttempts,
            int currentAttempt
    ) {
        super(id, lifecycleStatus, code, title, summary, deadline, minScoreToPass, status, courseId);
        this.maxAttempts = validateMaxAttempts(maxAttempts);
        this.currentAttempt = validateCurrentAttempt(currentAttempt);
    }

    // endregion

    // region Factories

    public static @NonNull Task create(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String courseId,
            int maxAttempts
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.future(deadline),
                minScoreToPass,
                AssignmentStatus.NOT_STARTED,
                courseId,
                maxAttempts,
                0
        );
    }

    public static @NonNull Task createHistorical(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String courseId,
            int maxAttempts
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.historical(deadline),
                minScoreToPass,
                AssignmentStatus.NOT_STARTED,
                courseId,
                maxAttempts,
                0
        );
    }

    // endregion

    // region Validations

    public int validateMaxAttempts(int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new InvalidMaxAttemptsException();
        }
        return maxAttempts;
    }

    public int validateCurrentAttempt(int currentAttempt) {
        if (currentAttempt < 0 || currentAttempt > this.maxAttempts) {
            throw new InvalidCurrentAttemptException();
        }
        return currentAttempt;
    }

    // endregion

    public void registerAttemptResult(@NonNull AssignmentScore score) {

        this.currentAttempt++;

        if (score.isGreaterOrEqualThan(this.minScoreToPass)) {
            this.status = AssignmentStatus.COMPLETED;
            return;
        }

        if (deadline.isOverdue()) {
            this.status = AssignmentStatus.FAILED;
            return;
        }

        this.status = AssignmentStatus.IN_PROGRESS;
    }

    public void markAsFailedIfNotCompleted() {
        if (this.status == AssignmentStatus.COMPLETED) return;

        if (deadline.isOverdue()) {
            this.status = AssignmentStatus.FAILED;
        }
    }

    @Override
    public String toString() {
        return String.format("TSK-%s", formatCode());

    }
}
