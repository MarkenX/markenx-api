package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AcademicTermQueryUseCase {
    AcademicTerm save(SaveAcademicTermCommand command);
    List<AcademicTerm> getAll();
    Page<@NotNull AcademicTermDTO> getAllPaginated(GetAllAcademicTermsPaginatedQuery query);
}
