package com.udla.markenx.api.academicterms.domain.ports.outgoing;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

import java.util.List;

public interface AcademicTermRepository {
    List<AcademicTerm> findAll();
}
