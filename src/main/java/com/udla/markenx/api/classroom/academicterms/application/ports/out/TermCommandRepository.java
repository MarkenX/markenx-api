package com.udla.markenx.api.classroom.academicterms.application.ports.out;

import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface TermCommandRepository {
    AcademicTerm save(AcademicTerm term);
    AcademicTerm findById(String id);
}
