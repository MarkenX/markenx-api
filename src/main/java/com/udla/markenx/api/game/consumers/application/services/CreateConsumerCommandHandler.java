package com.udla.markenx.api.game.consumers.application.services;

import com.udla.markenx.api.game.consumers.application.commands.CreateConsumerCommand;
import com.udla.markenx.api.game.consumers.application.ports.incoming.CreateConsumerUseCase;
import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.consumers.domain.ports.outgoing.ConsumerCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateConsumerCommandHandler implements CreateConsumerUseCase {

    private final ConsumerCommandRepository repository;

    @Override
    public Consumer handle(@NonNull CreateConsumerCommand command) {
        Consumer consumer = Consumer.create(
                command.name(),
                command.age(),
                command.budget(),
                command.targetAcceptanceScore()
        );

        return repository.save(consumer);
    }
}
