package com.udla.markenx.api.classroom.terms.application.handlers;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.mappers.TermPortMapper;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.DateInterval;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.services.TermDomainService;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateTermHandler implements CreateTermUseCase {

    private final TermCommandRepository commandRepository;
    private final TermQueryRepository queryRepository;
    private final TermPortMapper mapper = new TermPortMapper();

    @Override
    public TermPortDTO handle(@NotNull CreateTermCommand command) {
        List<Term> terms = queryRepository.findAllByLifecycleStatus(LifecycleStatus.ACTIVE.name());
        int sequence = TermDomainService.calculateSequence(terms, null);
        var dateInterval = new DateInterval(command.startDate(), command.endDate());

        Term newTerm;
        if (command.isHistorical()) {
            newTerm = Term.createHistoricalTerm(command.year(), sequence, dateInterval);
            if (newTerm.hasEnded()) {
                newTerm.disable();
            }
        } else {
            newTerm = Term.createTerm(command.year(), sequence, dateInterval);
        }

        TermDomainService.validateNoOverlaps(terms, newTerm);
        return mapper.toDTO(commandRepository.save(newTerm));
    }
}
