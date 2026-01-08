package com.udla.markenx.api.game.actions.application.services;

import com.udla.markenx.api.game.actions.application.commands.CreateActionCommand;
import com.udla.markenx.api.game.actions.application.ports.incoming.CreateActionUseCase;
import com.udla.markenx.api.game.actions.domain.exceptions.InvalidActionCategoryException;
import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionCategory;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import com.udla.markenx.api.game.actions.domain.ports.outgoing.ActionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateActionCommandHandler implements CreateActionUseCase {

    private final ActionCommandRepository repository;

    @Override
    public Action handle(@NonNull CreateActionCommand command) {
        ActionCategory category = parseCategory(command.category());

        List<ActionEffect> effects = command.effects() != null
                ? command.effects().stream()
                    .map(dto -> new ActionEffect(dto.dimensionId(), dto.delta()))
                    .collect(Collectors.toList())
                : List.of();

        Action action = command.id() != null
                ? Action.createWithId(
                        command.id(),
                        command.name(),
                        command.description(),
                        command.cost(),
                        category,
                        command.isInitiallyLocked(),
                        command.prerequisiteActionId(),
                        effects
                )
                : Action.create(
                        command.name(),
                        command.description(),
                        command.cost(),
                        category,
                        command.isInitiallyLocked(),
                        command.prerequisiteActionId(),
                        effects
                );

        Action savedAction = repository.save(action);

        if (!effects.isEmpty()) {
            repository.saveEffects(savedAction.getId().value(), effects);
        }

        return savedAction;
    }

    private ActionCategory parseCategory(String category) {
        try {
            return ActionCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidActionCategoryException();
        }
    }
}
