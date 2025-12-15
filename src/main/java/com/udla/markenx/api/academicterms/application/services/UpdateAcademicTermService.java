package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.academicterms.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.ports.incoming.UpdateAcademicTermUseCase;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAcademicTermService implements UpdateAcademicTermUseCase {

    private final AcademicTermRepository repository;

    @Override
    public AcademicTerm update(UpdateAcademicTermCommand command) {
        AcademicTerm term = repository.findById(command.id());
        term.update(command.startDate(), command.endDate(), command.year());
        return repository.save(term);
    }

    @Override
    public AcademicTerm changeStatus(ChangeAcademicTermStatusCommand command) {
        AcademicTerm term = repository.findById(command.id());
        switch (command.targetStatus()) {
            case ACTIVE -> term.enable();
            case DISABLED -> term.disable();
        }
        return repository.save(term);
    }
}
