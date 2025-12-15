package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
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
    public AcademicTerm disable(ChangeAcademicTermStatusCommand command) {
        return repository.changeStatus(command.id(), command.targetStatus());
    }
}
