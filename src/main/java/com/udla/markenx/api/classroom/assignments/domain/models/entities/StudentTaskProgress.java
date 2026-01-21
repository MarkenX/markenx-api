package com.udla.markenx.api.classroom.assignments.domain.models.entities;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.InvalidCurrentAttemptException;
import org.jspecify.annotations.NonNull;

/**
 * Entity representing a student's progress on a specific task.
 * Tracks the current attempt number per (student, task) combination.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public class StudentTaskProgress {

    private final String studentId;
    private final String taskId;
    private int currentAttempt;

    // region Constructors

    public StudentTaskProgress(
            @NonNull String studentId,
            @NonNull String taskId,
            int currentAttempt
    ) {
        this.studentId = validateStudentId(studentId);
        this.taskId = validateTaskId(taskId);
        this.currentAttempt = validateCurrentAttempt(currentAttempt);
    }

    // endregion

    // region Factories

    /**
     * Creates a new StudentTaskProgress with zero attempts.
     */
    public static @NonNull StudentTaskProgress create(
            @NonNull String studentId,
            @NonNull String taskId
    ) {
        return new StudentTaskProgress(studentId, taskId, 0);
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
