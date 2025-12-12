package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

public interface AcademicTermQueryUseCase {
    Page<@NotNull AcademicTermDTO> getAllPaginated(GetAllAcademicTermsPaginatedQuery query);
}
