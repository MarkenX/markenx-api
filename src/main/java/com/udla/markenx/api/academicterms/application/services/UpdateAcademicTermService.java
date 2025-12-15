package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.commands.DisableAcademicTermCommand;
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
    public AcademicTerm disable(DisableAcademicTermCommand command) {
        return repository.disable(command.id());
    }
}
