package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.ports.incoming.AcademicTermQueryUseCase;
import com.udla.markenx.api.academicterms.application.queries.GetAllAcademicTermsPaginatedQuery;
import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.DateInterval;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import com.udla.markenx.api.academicterms.domain.services.AcademicTermDomainService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTermQueryService implements AcademicTermQueryUseCase {

    private final AcademicTermRepository repository;

    @Override
    public AcademicTerm save(@NotNull SaveAcademicTermCommand command) {
        List<AcademicTerm> terms = repository.findAllByYear(command.year());
        int sequence = AcademicTermDomainService.calculateSequence(terms, null);
        var dateInterval = new DateInterval(command.startDate(), command.endDate());

        AcademicTerm newTerm;
        if (command.isHistorical()) {
            newTerm = AcademicTerm.createHistoricalTerm(command.year(), sequence, dateInterval);
        } else {
            newTerm = AcademicTerm.createTerm(command.year(), sequence, dateInterval);
        }

        AcademicTermDomainService.validateNoOverlaps(terms, newTerm);
        return repository.save(newTerm);
    }

    @Override
    public List<AcademicTerm> getAll() {
        return repository.findAll();
    }

    @Override
    public List<AcademicTerm> getAllExcludingStatus(AcademicTermStatus status) {
        return repository.findByStatusNot(status);
    }

    @Override
    public Page<@NotNull AcademicTerm> getAllPaginated(@NotNull GetAllAcademicTermsPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }
}
