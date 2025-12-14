package com.udla.markenx.api.academicterms.domain.models.valueobjects;

import lombok.Getter;

/**
 * Represents the status of an academic term.
 * <p>
 * This enumeration allows classification of academic terms into three main states:
 * - ACTIVE: Indicates the academic term is currently ongoing.
 * - ENDED: Indicates the academic term has concluded.
 * - UPCOMING: Indicates the academic term is scheduled to begin in the future.
 * <p>
 * Each status is associated with a label providing a descriptive value
 * in the form of a string, primarily for display or localization purposes.
 */
@Getter
public enum AcademicTermStatus {
    ACTIVE("En curso"),
    ENDED("Finalizado"),
    UPCOMING("Próximo");

    private final String label;

    AcademicTermStatus(String label) {
        this.label = label;
    }
}
