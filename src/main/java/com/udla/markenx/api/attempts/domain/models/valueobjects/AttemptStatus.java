package com.udla.markenx.api.attempts.domain.models.valueobjects;

public enum AttemptStatus {
    APPROVED("Aprobado"),
    DISAPPROVED("Reprobado"),
    UNKNOWN("Desconocido");

    private final String label;

    AttemptStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
