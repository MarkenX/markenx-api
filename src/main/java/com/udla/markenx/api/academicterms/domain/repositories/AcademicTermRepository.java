package com.udla.markenx.api.academicterms.domain.repositories;

import com.udla.markenx.api.academicterms.domain.models.AcademicTerm;

import java.util.List;

public interface AcademicTermRepository {
    List<AcademicTerm> findAll();
}
