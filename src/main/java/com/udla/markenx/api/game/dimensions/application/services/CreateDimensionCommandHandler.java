package com.udla.markenx.api.game.dimensions.application.services;

import com.udla.markenx.api.game.dimensions.application.commands.CreateDimensionCommand;
import com.udla.markenx.api.game.dimensions.application.ports.incoming.CreateDimensionUseCase;
import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.dimensions.domain.ports.outgoing.DimensionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDimensionCommandHandler implements CreateDimensionUseCase {

    private final DimensionCommandRepository repository;

    @Override
    public Dimension handle(@NonNull CreateDimensionCommand command) {
        Dimension dimension = command.id() != null
                ? Dimension.createWithId(
                        command.id(),
                        command.name(),
                        command.displayName(),
                        command.description(),
                        command.consumerExpectation(),
                        command.productInitialOffer()
                )
                : Dimension.create(
                        command.name(),
                        command.displayName(),
                        command.description(),
                        command.consumerExpectation(),
                        command.productInitialOffer()
                );

        return repository.save(dimension);
    }
}
