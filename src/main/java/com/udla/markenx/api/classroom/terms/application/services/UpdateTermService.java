package com.udla.markenx.api.classroom.terms.application.services;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.mappers.TermPortMapper;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.UpdateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateTermService implements UpdateTermUseCase {

    private final TermCommandRepository commandRepository;
    private final TermQueryRepository queryRepository;
    private final TermPortMapper mapper = new TermPortMapper();

    @Override
    public TermPortDTO update(@NonNull UpdateTermCommand command) {
        Term term = queryRepository.findByIdOrThrow(command.id());
        term.update(command.startDate(), command.endDate(), command.year());
        return mapper.toDTO(commandRepository.save(term));
    }

    @Override
    public TermPortDTO changeStatus(@NonNull ChangeTermStatusCommand command) {
        var term = queryRepository.findByIdOrThrow(command.id());
        switch (command.targetStatus()) {
            case LifecycleStatus.ACTIVE -> term.enable();
            case LifecycleStatus.DISABLED -> term.disable();
        }
        return mapper.toDTO(commandRepository.save(term));
    }

    @Override
    public TermPortDTO getById(@NotNull TermIdQuery query) {
        return mapper.toDTO(queryRepository.findByIdOrThrow(query.id()));
    }
}
