package com.udla.markenx.api.classroom.academicterms.domain.ports.outgoing;

import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface AcademicTermCommandRepository {
    AcademicTerm save(AcademicTerm term);
    AcademicTerm findById(String id);
}
