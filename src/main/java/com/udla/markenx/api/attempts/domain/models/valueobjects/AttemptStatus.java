package com.udla.markenx.api.attempts.domain.models.valueobjects;

public enum AttemptStatus {
    APPROVED("Aprobado") {
        @Override
        public boolean canTransitionTo(AttemptStatus next) {
            return false;
        }
    },

    DISAPPROVED("Reprobado") {
        @Override
        public boolean canTransitionTo(AttemptStatus next) {
            return false;
        }
    },

    UNKNOWN("Desconocido") {
        @Override
        public boolean canTransitionTo(AttemptStatus next) {
            return next == APPROVED || next == DISAPPROVED;
        }
    };

    private final String label;

    AttemptStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public abstract boolean canTransitionTo(AttemptStatus next);
}