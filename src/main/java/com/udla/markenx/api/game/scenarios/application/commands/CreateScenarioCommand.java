package com.udla.markenx.api.game.scenarios.application.commands;

import com.udla.markenx.api.game.scenarios.infrastructure.web.rest.dtos.CreateScenarioRequestDTO;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateScenarioCommand(
        String title,
        String description,
        ConsumerDTO consumer,
        List<DimensionDTO> dimensions,
        List<ActionDTO> actions,
        List<EventDTO> events
) {
    public record ConsumerDTO(
            String id,
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
    }

    public record DimensionDTO(
            String id,
            String name,
            String displayName,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
    }

    public record ActionDTO(
            String id,
            String name,
            String description,
            BigDecimal cost,
            String category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffectDTO> effects
    ) {
    }

    public record ActionEffectDTO(
            String dimensionId,
            double delta
    ) {
    }

    public record EventDTO(
            String id,
            String title,
            String description,
            List<EventEffectDTO> effects
    ) {
    }

    public record EventEffectDTO(
            String dimensionId,
            double weightMultiplier
    ) {
    }

    @Contract("_ -> new")
    public static @NonNull CreateScenarioCommand from(@NonNull CreateScenarioRequestDTO dto) {
        return new CreateScenarioCommand(
                dto.title(),
                dto.description(),
                toConsumerDTO(dto.consumer()),
                toDimensionDTOs(dto.dimensions()),
                toActionDTOs(dto.actions()),
                toEventDTOs(dto.events())
        );
    }

    private static ConsumerDTO toConsumerDTO(CreateScenarioRequestDTO.ConsumerRequestDTO dto) {
        if (dto == null) return null;
        return new ConsumerDTO(
                dto.id(),
                dto.name(),
                dto.age(),
                dto.budget(),
                dto.targetAcceptanceScore()
        );
    }

    private static List<DimensionDTO> toDimensionDTOs(List<CreateScenarioRequestDTO.DimensionRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(d -> new DimensionDTO(
                        d.id(),
                        d.name(),
                        d.displayName(),
                        d.description(),
                        d.consumerExpectation(),
                        d.productInitialOffer()
                ))
                .toList();
    }

    private static List<ActionDTO> toActionDTOs(List<CreateScenarioRequestDTO.ActionRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(a -> new ActionDTO(
                        a.id(),
                        a.name(),
                        a.description(),
                        a.cost(),
                        a.category(),
                        a.isInitiallyLocked(),
                        a.prerequisiteActionId(),
                        a.effects() != null
                                ? a.effects().stream()
                                    .map(e -> new ActionEffectDTO(e.dimensionId(), e.delta()))
                                    .toList()
                                : null
                ))
                .toList();
    }

    private static List<EventDTO> toEventDTOs(List<CreateScenarioRequestDTO.EventRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(e -> new EventDTO(
                        e.id(),
                        e.title(),
                        e.description(),
                        e.effects() != null
                                ? e.effects().stream()
                                    .map(ef -> new EventEffectDTO(ef.dimensionId(), ef.weightMultiplier()))
                                    .toList()
                                : null
                ))
                .toList();
    }
}
