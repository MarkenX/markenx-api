package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.dtos.AcademicTermDTO;
import com.udla.markenx.api.academicterms.application.mappers.AcademicTermDTOMapper;
import com.udla.markenx.api.academicterms.application.ports.incoming.AcademicTermQueryUseCase;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AcademicTermQueryService implements AcademicTermQueryUseCase {

    private final AcademicTermRepository repository;
    private final AcademicTermDTOMapper mapper;

    public AcademicTermQueryService(AcademicTermRepository repository,
                                    AcademicTermDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<@NotNull AcademicTermDTO> getAllPaginated(GetAllAcademicTermsPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable).map(mapper::toDTO);
    }
}
