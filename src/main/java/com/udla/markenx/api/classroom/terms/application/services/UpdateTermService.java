package com.udla.markenx.api.classroom.terms.application.services;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.mappers.TermPortMapper;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.UpdateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQueryCriteria;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTermService implements UpdateTermUseCase {

    private final TermCommandRepository repository;
    private final TermPortMapper mapper = new TermPortMapper();

    @Override
    public TermPortDTO update(@NonNull UpdateTermCommand command) {
        AcademicTerm term = repository.findById(command.id());
        term.update(command.startDate(), command.endDate(), command.year());
        return mapper.toDTO(repository.save(term));
    }

    @Override
    public TermPortDTO changeStatus(@NonNull ChangeTermStatusCommand command) {
        var term = repository.findById(command.id());
        switch (command.targetStatus()) {
            case LifecycleStatus.ACTIVE -> term.enable();
            case LifecycleStatus.DISABLED -> term.disable();
        }
        return mapper.toDTO(repository.save(term));
    }

    @Override
    public TermPortDTO getById(@NotNull TermIdQueryCriteria query) {
        return mapper.toDTO(repository.findById(query.id()));
    }
}
