package com.udla.markenx.api.students.domain.models.valueobjects;

/**
 * Represents the various statuses a student can have within the system.
 * Each status represents a stage in the student's lifecycle, and certain
 * transitions between statuses are permitted based on business rules.
 */
public enum StudentStatus {

    /**
     * The student has been registered but does not yet have a technical identity.
     * <p>
     * This state is typically entered immediately after registration and
     * indicates that the identity provisioning process has started but
     * has not yet completed.
     */
    PENDING_IDENTITY {
        @Override
        public boolean canTransitionTo(StudentStatus next) {
            return next == ACTIVE || next == IDENTITY_CREATION_FAILED;
        }
    },

    /**
     * The student is fully active from a business perspective.
     * <p>
     * This state indicates that the student's identity has been successfully
     * created and linked, and the student can participate in all business
     * processes.
     * <p>
     * <strong>Important:</strong> This state does NOT imply that the entity
     * is enabled or disabled at a lifecycle level. That concern is handled
     * separately by {@code LifecycleStatus}.
     */
    ACTIVE {
        @Override
        public boolean canTransitionTo(StudentStatus next) {
            return false;
        }
    },

    /**
     * The identity creation process failed.
     * <p>
     * This state indicates that the student registration succeeded, but the
     * identity provisioning process did not complete successfully.
     * <p>
     * From this state, the system may retry the identity creation process.
     */
    IDENTITY_CREATION_FAILED {
        @Override
        public boolean canTransitionTo(StudentStatus next) {
            return next == PENDING_IDENTITY;
        }
    };

    /**
     * Determines whether the current status can transition to the given next status.
     *
     * @param next the target {@code StudentStatus}
     * @return {@code true} if the transition is allowed, {@code false} otherwise
     */
    public abstract boolean canTransitionTo(StudentStatus next);
}
