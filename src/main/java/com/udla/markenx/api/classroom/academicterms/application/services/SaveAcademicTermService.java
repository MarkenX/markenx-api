package com.udla.markenx.api.classroom.academicterms.application.services;

import com.udla.markenx.api.classroom.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.incoming.SaveAcademicTermUseCase;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.DateInterval;
import com.udla.markenx.api.classroom.academicterms.domain.ports.outgoing.AcademicTermCommandRepository;
import com.udla.markenx.api.classroom.academicterms.domain.ports.outgoing.AcademicTermQueryRepository;
import com.udla.markenx.api.classroom.academicterms.domain.services.AcademicTermDomainService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveAcademicTermService implements SaveAcademicTermUseCase {

    private final AcademicTermCommandRepository commandRepository;
    private final AcademicTermQueryRepository queryRepository;

    @Override
    public AcademicTerm handle(@NotNull SaveAcademicTermCommand command) {
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
        return commandRepository.save(newTerm);
    }
}
