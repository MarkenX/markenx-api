package com.udla.markenx.api.classroom.terms.application.ports.out;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;

public interface TermCommandRepository {
    AcademicTerm save(AcademicTerm term);
    AcademicTerm findById(String id);
    void ensureExists(String id);
    void ensureIsUpcoming(String id);
}
