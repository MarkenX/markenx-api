package com.udla.markenx.api.game.events.application.services;

import com.udla.markenx.api.game.events.application.commands.CreateGameEventCommand;
import com.udla.markenx.api.game.events.application.ports.incoming.CreateGameEventUseCase;
import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import com.udla.markenx.api.game.events.domain.ports.outgoing.GameEventCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateGameEventCommandHandler implements CreateGameEventUseCase {

    private final GameEventCommandRepository repository;

    @Override
    public GameEvent handle(@NonNull CreateGameEventCommand command) {
        List<EventEffect> effects = command.effects() != null
                ? command.effects().stream()
                    .map(dto -> new EventEffect(dto.dimensionId(), dto.weightMultiplier()))
                    .collect(Collectors.toList())
                : List.of();

        GameEvent event = GameEvent.create(
                command.title(),
                command.description(),
                effects
        );

        GameEvent savedEvent = repository.save(event);

        if (!effects.isEmpty()) {
            repository.saveEffects(savedEvent.getId().value(), effects);
        }

        return savedEvent;
    }
}
