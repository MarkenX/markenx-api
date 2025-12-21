package com.udla.markenx.api.academicterms.application.services;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.academicterms.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.ports.incoming.UpdateAcademicTermUseCase;
import com.udla.markenx.api.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermCommandRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAcademicTermService implements UpdateAcademicTermUseCase {

    private final AcademicTermCommandRepository repository;

    @Override
    public AcademicTerm update(@NonNull UpdateAcademicTermCommand command) {
        AcademicTerm term = repository.findById(command.id());
        term.update(command.startDate(), command.endDate(), command.year());
        return repository.save(term);
    }

    @Override
    public AcademicTerm changeStatus(@NonNull ChangeAcademicTermStatusCommand command) {
        AcademicTerm term = repository.findById(command.id());
        if (command.targetStatus() == LifecycleStatus.ACTIVE) {
            term.enable();
        } else {
            term.disable();
        }
        return repository.save(term);
    }

    @Override
    public AcademicTerm getById(@NotNull GetAcademicTermByIdQuery query) {
        return repository.findById(query.id());
    }
}
