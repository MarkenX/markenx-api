package com.udla.markenx.api.assignments.domain.models.valueobjects;

/**
 * Represents the lifecycle status of an Assignment.
 * Each status defines which transitions are allowed
 * according to business rules.
 */
@SuppressWarnings({"LombokGetterMayBeUsed"})
public enum AssignmentStatus {

    NOT_STARTED("Sin empezar") {
        @Override
        public boolean canTransitionTo(AssignmentStatus next) {
            return next == IN_PROGRESS || next == OUTDATED;
        }
    },

    IN_PROGRESS("En curso") {
        @Override
        public boolean canTransitionTo(AssignmentStatus next) {
            return next == COMPLETED || next == FAILED || next == OUTDATED;
        }
    },

    COMPLETED("Completada") {
        @Override
        public boolean canTransitionTo(AssignmentStatus next) {
            return false;
        }
    },

    FAILED("Fallida") {
        @Override
        public boolean canTransitionTo(AssignmentStatus next) {
            return false;
        }
    },

    OUTDATED("Vencida") {
        @Override
        public boolean canTransitionTo(AssignmentStatus next) {
            return false;
        }
    };

    private final String label;

    AssignmentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Determines whether the current status can transition
     * to the given next status.
     *
     * @param next the target {@code AssignmentStatus}
     * @return {@code true} if the transition is allowed
     */
    public abstract boolean canTransitionTo(AssignmentStatus next);
}
