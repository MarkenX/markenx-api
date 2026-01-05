package com.udla.markenx.api.classroom.academicterms.application.ports.incoming;

import com.udla.markenx.api.classroom.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.academicterms.domain.models.valueobjects.AcademicTermStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AcademicTermQueryUseCase {
    List<AcademicTerm> getAll();
    List<AcademicTerm> getAllExcludingStatus(AcademicTermStatus status);
    Page<@NotNull AcademicTerm> getAllPaginated(GetAllAcademicTermsPaginatedQuery query);
}
