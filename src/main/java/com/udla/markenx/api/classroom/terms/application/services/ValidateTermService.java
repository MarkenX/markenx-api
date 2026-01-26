package com.udla.markenx.api.classroom.terms.application.services;

import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ValidateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTermService implements ValidateTermUseCase {

    private final TermQueryRepository repository;

    @Override
    public boolean isUpcoming(@NonNull IsUpcomingTermQuery query) {
        Term term = repository.findByIdOrThrow(query.id());
        return term.isUpcoming();
    }
}
