package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AcademicTermQueryUseCase {
    AcademicTerm getById(GetAcademicTermByIdQuery query);
    List<AcademicTerm> getAll();
    List<AcademicTerm> getAllExcludingStatus(AcademicTermStatus status);
    Page<@NotNull AcademicTerm> getAllPaginated(GetAllAcademicTermsPaginatedQuery query);
}
