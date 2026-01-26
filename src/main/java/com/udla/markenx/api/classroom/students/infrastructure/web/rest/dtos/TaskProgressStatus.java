package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import org.jspecify.annotations.NonNull;

/**
 * REST-layer enum representing the status of a student's task progress.
 * Maps from domain {@link AssignmentStatus} for API responses.
 *
 * <p>Task lifecycle:</p>
 * <ul>
 *   <li>{@code NOT_STARTED} - No attempts made yet</li>
 *   <li>{@code IN_PROGRESS} - At least one attempt made, but not yet completed or failed</li>
 *   <li>{@code COMPLETED} - At least one attempt has finalOutcome WIN</li>
 *   <li>{@code FAILED} - All attempts exhausted with LOSE outcomes</li>
 *   <li>{@code OUTDATED} - Task expired with no attempts made</li>
 * </ul>
 */
public enum TaskProgressStatus {

    /**
     * No attempts have been made yet.
     */
    NOT_STARTED,

    /**
     * At least one attempt has been made, but the task is not yet completed or failed.
     * Attempts remain and no WIN finalOutcome achieved.
     */
    IN_PROGRESS,

    /**
     * The task has been successfully completed.
     * At least one attempt has finalOutcome WIN.
     */
    COMPLETED,

    /**
     * All attempts have been exhausted with LOSE outcomes.
     */
    FAILED,

    /**
     * The task deadline passed with no attempts made.
     */
    OUTDATED;

    /**
     * Maps from domain AssignmentStatus to REST TaskProgressStatus.
     *
     * @param status the domain assignment status
     * @return the corresponding REST status
     */
    public static @NonNull TaskProgressStatus from(@NonNull AssignmentStatus status) {
        return switch (status) {
            case NOT_STARTED -> NOT_STARTED;
            case IN_PROGRESS -> IN_PROGRESS;
            case COMPLETED -> COMPLETED;
            case FAILED -> FAILED;
            case OUTDATED -> OUTDATED;
        };
    }

    /**
     * Maps from domain AssignmentStatus name string to REST TaskProgressStatus.
     *
     * @param statusName the domain assignment status name
     * @return the corresponding REST status
     */
    public static @NonNull TaskProgressStatus fromName(@NonNull String statusName) {
        return from(AssignmentStatus.valueOf(statusName));
    }
}
