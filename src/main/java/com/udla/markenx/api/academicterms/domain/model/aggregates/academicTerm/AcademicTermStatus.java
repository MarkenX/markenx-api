package com.udla.markenx.api.academicterms.domain.model.aggregates.academicTerm;

import lombok.Getter;

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
