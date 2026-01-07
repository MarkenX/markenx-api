package com.udla.markenx.api.game.scenarios.infrastructure.web.rest.mappers;

import com.udla.markenx.api.game.scenarios.application.commands.CreateScenarioCommand;
import com.udla.markenx.api.game.scenarios.infrastructure.web.rest.dtos.CreateScenarioRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScenarioRequestMapper {

    public CreateScenarioCommand toCommand(CreateScenarioRequestDTO dto) {
        return new CreateScenarioCommand(
                dto.title(),
                dto.description(),
                toConsumerDTO(dto.consumer()),
                toDimensionDTOs(dto.dimensions()),
                toActionDTOs(dto.actions()),
                toEventDTOs(dto.events())
        );
    }

    private CreateScenarioCommand.ConsumerDTO toConsumerDTO(CreateScenarioRequestDTO.ConsumerRequestDTO dto) {
        if (dto == null) return null;
        return new CreateScenarioCommand.ConsumerDTO(
                dto.name(),
                dto.age(),
                dto.budget(),
                dto.targetAcceptanceScore()
        );
    }

    private List<CreateScenarioCommand.DimensionDTO> toDimensionDTOs(List<CreateScenarioRequestDTO.DimensionRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(d -> new CreateScenarioCommand.DimensionDTO(
                        d.id(),
                        d.name(),
                        d.displayName(),
                        d.description(),
                        d.consumerExpectation(),
                        d.productInitialOffer()
                ))
                .collect(Collectors.toList());
    }

    private List<CreateScenarioCommand.ActionDTO> toActionDTOs(List<CreateScenarioRequestDTO.ActionRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(a -> new CreateScenarioCommand.ActionDTO(
                        a.id(),
                        a.name(),
                        a.description(),
                        a.cost(),
                        a.category(),
                        a.isInitiallyLocked(),
                        a.prerequisiteActionId(),
                        a.effects() != null
                                ? a.effects().stream()
                                    .map(e -> new CreateScenarioCommand.ActionEffectDTO(e.dimensionId(), e.delta()))
                                    .collect(Collectors.toList())
                                : null
                ))
                .collect(Collectors.toList());
    }

    private List<CreateScenarioCommand.EventDTO> toEventDTOs(List<CreateScenarioRequestDTO.EventRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(e -> new CreateScenarioCommand.EventDTO(
                        e.id(),
                        e.title(),
                        e.description(),
                        e.effects() != null
                                ? e.effects().stream()
                                    .map(ef -> new CreateScenarioCommand.EventEffectDTO(ef.dimensionId(), ef.weightMultiplier()))
                                    .collect(Collectors.toList())
                                : null
                ))
                .collect(Collectors.toList());
    }
}
