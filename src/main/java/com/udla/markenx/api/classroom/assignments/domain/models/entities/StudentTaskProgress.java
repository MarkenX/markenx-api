package com.udla.markenx.api.classroom.assignments.domain.models.entities;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidAssignmentStatusTransitionException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidCurrentAttemptException;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * Entity representing a student's progress on a specific task.
 * Tracks the current attempt number and status per (student, task) combination.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public class StudentTaskProgress {

    private final String studentId;
    private final String taskId;
    private int currentAttempt;
    private AssignmentStatus status;

    // region Constructors

    public StudentTaskProgress(
            @NonNull String studentId,
            @NonNull String taskId,
            int currentAttempt,
            @NonNull AssignmentStatus status
    ) {
        this.studentId = validateStudentId(studentId);
        this.taskId = validateTaskId(taskId);
        this.currentAttempt = validateCurrentAttempt(currentAttempt);
        this.status = status;
    }

    // endregion

    // region Factories

    /**
     * Creates a new StudentTaskProgress with zero attempts and NOT_STARTED status.
     */
    public static @NonNull StudentTaskProgress create(
            @NonNull String studentId,
            @NonNull String taskId
    ) {
        return new StudentTaskProgress(studentId, taskId, 0, AssignmentStatus.NOT_STARTED);
    }

    // endregion

    // region Getters

    public String getStudentId() {
        return this.studentId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public int getCurrentAttempt() {
        return this.currentAttempt;
    }

    public AssignmentStatus getStatus() {
        return this.status;
    }

    // endregion

    // region Business Logic

    /**
     * Increments the current attempt counter.
     * Should be called when a new attempt is registered.
     */
    public void incrementAttempt() {
        this.currentAttempt++;
    }

    /**
     * Calculates the remaining attempts based on max attempts allowed.
     *
     * @param maxAttempts The maximum attempts allowed for the task
     * @return The number of remaining attempts
     */
    public int getRemainingAttempts(int maxAttempts) {
        return Math.max(0, maxAttempts - currentAttempt);
    }

    /**
     * Checks if the student has exhausted all attempts.
     *
     * @param maxAttempts The maximum attempts allowed for the task
     * @return true if no attempts remain
     */
    public boolean hasExhaustedAttempts(int maxAttempts) {
        return currentAttempt >= maxAttempts;
    }

    /**
     * Registers the result of an attempt and updates status accordingly.
     *
     * <p>Status transitions:</p>
     * <ul>
     *   <li>If score >= minScoreToPass → COMPLETED (WIN)</li>
     *   <li>If all attempts exhausted with LOSE outcomes → FAILED</li>
     *   <li>Otherwise → IN_PROGRESS</li>
     * </ul>
     *
     * @param score The score achieved in the attempt
     * @param minScoreToPass The minimum score required to pass
     * @param maxAttempts The maximum attempts allowed
     * @param deadline The task deadline
     */
    public void registerAttemptResult(
            @NonNull AssignmentScore score,
            @NonNull AssignmentScore minScoreToPass,
            int maxAttempts,
            @NonNull LocalDateTime deadline
    ) {
        if (isCompleted() || isFailed() || isOutdated()) {
            return;
        }

        if (score.isGreaterOrEqualThan(minScoreToPass)) {
            transitionTo(AssignmentStatus.COMPLETED);
            return;
        }

        // All attempts exhausted with LOSE outcomes → FAILED
        if (currentAttempt >= maxAttempts) {
            transitionTo(AssignmentStatus.FAILED);
            return;
        }

        transitionTo(AssignmentStatus.IN_PROGRESS);
    }

    /**
     * Marks this progress based on deadline expiration.
     *
     * <p>Status transitions when overdue:</p>
     * <ul>
     *   <li>If no attempts made (currentAttempt == 0) → OUTDATED</li>
     *   <li>If attempts made but not completed → FAILED</li>
     * </ul>
     *
     * @param deadline The task deadline
     */
    public void markAsExpiredIfOverdue(@NonNull LocalDateTime deadline) {
        if (isCompleted() || isFailed() || isOutdated()) {
            return;
        }

        if (isOverdue(deadline)) {
            if (currentAttempt == 0) {
                transitionTo(AssignmentStatus.OUTDATED);
            } else {
                transitionTo(AssignmentStatus.FAILED);
            }
        }
    }

    /**
     * @deprecated Use {@link #markAsExpiredIfOverdue(LocalDateTime)} instead.
     */
    @Deprecated(forRemoval = true)
    public void markAsFailedIfOverdue(@NonNull LocalDateTime deadline) {
        markAsExpiredIfOverdue(deadline);
    }

    public boolean isCompleted() {
        return this.status == AssignmentStatus.COMPLETED;
    }

    public boolean isFailed() {
        return this.status == AssignmentStatus.FAILED;
    }

    public boolean isNotStarted() {
        return this.status == AssignmentStatus.NOT_STARTED;
    }

    public boolean isInProgress() {
        return this.status == AssignmentStatus.IN_PROGRESS;
    }

    public boolean isOutdated() {
        return this.status == AssignmentStatus.OUTDATED;
    }

    // endregion

    // region Status Transitions

    private void transitionTo(@NonNull AssignmentStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidAssignmentStatusTransitionException(this.status, newStatus);
        }
        this.status = newStatus;
    }

    private boolean isOverdue(@NonNull LocalDateTime deadline) {
        return LocalDateTime.now().isAfter(deadline);
    }

    // endregion

    // region Validations

    private @NonNull String validateStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID cannot be null or blank");
        }
        return studentId;
    }

    private @NonNull String validateTaskId(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            throw new IllegalArgumentException("Task ID cannot be null or blank");
        }
        return taskId;
    }

    private int validateCurrentAttempt(int currentAttempt) {
        if (currentAttempt < 0) {
            throw new InvalidCurrentAttemptException();
        }
        return currentAttempt;
    }

    // endregion
}
