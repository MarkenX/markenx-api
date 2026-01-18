package com.udla.markenx.api.classroom.terms.application.services;

import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.FilterMode;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermPageQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermStatusQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.mappers.TermPortMapper;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryTermsService implements QueryTermsUseCase {

    private final TermQueryRepository repository;
    private final TermPortMapper mapper = new TermPortMapper();

    @Override
    public List<TermPortDTO> listTerms() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public TermPortDTO getActiveTerm() {
        return repository.findActiveTerm()
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un periodo académico activo"
                ));
    }

    @Override
    public TermPortDTO getTermById(@NonNull TermIdQueryCriteria criteria) {
        return repository.findById(criteria.id())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un periodo académico con el id: " + criteria.id()
                ));
    }

    @Override
    public List<TermPortDTO> listTermsByStatus(@NonNull TermStatusQueryCriteria criteria) {
        boolean exclude = criteria.mode() == FilterMode.EXCLUDE;

        return repository.findAllByStatus(criteria.statuses(), exclude).stream()
                .map(mapper::toDTO)
                .toList();
    }


    @Override
    public Page<TermPortDTO> listTermsPage(@NotNull TermPageQueryCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        return repository.findAllPaginated(pageable)
                .map(mapper::toDTO);
    }
}
