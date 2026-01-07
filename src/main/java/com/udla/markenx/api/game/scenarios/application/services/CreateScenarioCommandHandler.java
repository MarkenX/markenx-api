package com.udla.markenx.api.game.scenarios.application.services;

import com.udla.markenx.api.game.actions.application.commands.CreateActionCommand;
import com.udla.markenx.api.game.actions.application.ports.incoming.CreateActionUseCase;
import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.consumers.application.commands.CreateConsumerCommand;
import com.udla.markenx.api.game.consumers.application.ports.incoming.CreateConsumerUseCase;
import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.dimensions.application.commands.CreateDimensionCommand;
import com.udla.markenx.api.game.dimensions.application.ports.incoming.CreateDimensionUseCase;
import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.events.application.commands.CreateGameEventCommand;
import com.udla.markenx.api.game.events.application.ports.incoming.CreateGameEventUseCase;
import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioResponse;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.CreateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.domain.events.ScenarioConfigurationCompletedEvent;
import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import com.udla.markenx.api.game.scenarios.domain.ports.outgoing.ScenarioCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateScenarioCommandHandler implements CreateScenarioUseCase {

    private final ScenarioCommandRepository scenarioRepo;
    private final CreateDimensionUseCase createDimensionUseCase;
    private final CreateConsumerUseCase createConsumerUseCase;
    private final CreateActionUseCase createActionUseCase;
    private final CreateGameEventUseCase createGameEventUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ScenarioResponse handle(@NonNull CreateScenarioCommand command) {
        // 1. Crear Scenario básico
        Scenario scenario = Scenario.create(command.title(), command.description());
        scenario = scenarioRepo.save(scenario);

        // 2. Procesar Dimensions
        List<String> dimensionIds = processDimensions(command.dimensions());
        scenarioRepo.addDimensions(scenario.getId(), dimensionIds);

        // 3. Crear Consumer
        Consumer consumer = processConsumer(command.consumer());
        scenarioRepo.associateConsumer(scenario.getId(), consumer.getId().value());

        // 4. Crear Actions con sus effects
        List<String> actionIds = processActions(command.actions());
        scenarioRepo.addActions(scenario.getId(), actionIds);

        // 5. Crear Events con sus effects
        List<String> eventIds = processEvents(command.events());
        scenarioRepo.addEvents(scenario.getId(), eventIds);

        // 6. Publicar evento de éxito
        eventPublisher.publishEvent(new ScenarioConfigurationCompletedEvent(
                scenario.getId(),
                consumer.getId().value(),
                dimensionIds,
                actionIds,
                eventIds
        ));

        return new ScenarioResponse(scenario.getId(), scenario.getTitle(), scenario.getDescription());
    }

    private List<String> processDimensions(List<CreateScenarioCommand.DimensionDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(dto -> {
                    CreateDimensionCommand cmd = new CreateDimensionCommand(
                            dto.name(),
                            dto.displayName(),
                            dto.description(),
                            dto.consumerExpectation(),
                            dto.productInitialOffer()
                    );
                    Dimension dimension = createDimensionUseCase.handle(cmd);
                    return dimension.getId().value();
                })
                .collect(Collectors.toList());
    }

    private Consumer processConsumer(CreateScenarioCommand.ConsumerDTO dto) {
        CreateConsumerCommand cmd = new CreateConsumerCommand(
                dto.name(),
                dto.age(),
                dto.budget(),
                dto.targetAcceptanceScore()
        );
        return createConsumerUseCase.handle(cmd);
    }

    private List<String> processActions(List<CreateScenarioCommand.ActionDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(dto -> {
                    List<CreateActionCommand.ActionEffectDTO> effects = dto.effects() != null
                            ? dto.effects().stream()
                                .map(e -> new CreateActionCommand.ActionEffectDTO(e.dimensionId(), e.delta()))
                                .collect(Collectors.toList())
                            : List.of();

                    CreateActionCommand cmd = new CreateActionCommand(
                            dto.name(),
                            dto.description(),
                            dto.cost(),
                            dto.category(),
                            dto.isInitiallyLocked(),
                            dto.prerequisiteActionId(),
                            effects
                    );
                    Action action = createActionUseCase.handle(cmd);
                    return action.getId().value();
                })
                .collect(Collectors.toList());
    }

    private List<String> processEvents(List<CreateScenarioCommand.EventDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(dto -> {
                    List<CreateGameEventCommand.EventEffectDTO> effects = dto.effects() != null
                            ? dto.effects().stream()
                                .map(e -> new CreateGameEventCommand.EventEffectDTO(e.dimensionId(), e.weightMultiplier()))
                                .collect(Collectors.toList())
                            : List.of();

                    CreateGameEventCommand cmd = new CreateGameEventCommand(
                            dto.title(),
                            dto.description(),
                            effects
                    );
                    GameEvent event = createGameEventUseCase.handle(cmd);
                    return event.getId().value();
                })
                .collect(Collectors.toList());
    }
}
