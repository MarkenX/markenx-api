package com.udla.markenx.api.classroom.terms.application.handlers;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.mappers.TermPortMapper;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.DateInterval;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.services.AcademicTermDomainService;
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
        List<AcademicTerm> terms = queryRepository.findAllByYear(command.year());
        int sequence = AcademicTermDomainService.calculateSequence(terms, null);
        var dateInterval = new DateInterval(command.startDate(), command.endDate());

        AcademicTerm newTerm;
        if (command.isHistorical()) {
            newTerm = AcademicTerm.createHistoricalTerm(command.year(), sequence, dateInterval);
        } else {
            newTerm = AcademicTerm.createTerm(command.year(), sequence, dateInterval);
        }

        AcademicTermDomainService.validateNoOverlaps(terms, newTerm);
        return mapper.toDTO(commandRepository.save(newTerm));
    }
}
