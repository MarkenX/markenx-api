package com.udla.markenx.api.game.scenarios.application.services;

import com.udla.markenx.api.game.actions.application.ports.incoming.ActionQueryUseCase;
import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import com.udla.markenx.api.game.consumers.application.ports.incoming.ConsumerQueryUseCase;
import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.dimensions.application.ports.incoming.DimensionQueryUseCase;
import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.events.application.ports.incoming.GameEventQueryUseCase;
import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioDetailResponse;
import com.udla.markenx.api.game.scenarios.application.dtos.ScenarioSummaryResponse;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ScenarioQueryUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.GetAllScenariosPaginatedQuery;
import com.udla.markenx.api.game.scenarios.application.queries.GetScenarioByIdQuery;
import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import com.udla.markenx.api.game.scenarios.domain.ports.outgoing.ScenarioQueryRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioQueryService implements ScenarioQueryUseCase {

    private final ScenarioQueryRepository scenarioRepo;
    private final ConsumerQueryUseCase consumerQuery;
    private final DimensionQueryUseCase dimensionQuery;
    private final ActionQueryUseCase actionQuery;
    private final GameEventQueryUseCase eventQuery;

    @Override
    public ScenarioDetailResponse getById(GetScenarioByIdQuery query) {
        Scenario scenario = scenarioRepo.findById(query.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el escenario con el identificador: " + query.id()
                ));

        // Get Consumer
        ScenarioDetailResponse.ConsumerResponse consumerResponse = null;
        Consumer consumer = consumerQuery.getByScenarioId(scenario.getId()).orElse(null);
        if (consumer != null) {
            consumerResponse = new ScenarioDetailResponse.ConsumerResponse(
                    consumer.getId().value(),
                    consumer.getName(),
                    consumer.getAge(),
                    consumer.getBudget(),
                    consumer.getTargetAcceptanceScore()
            );
        }

        // Get Dimensions
        List<Dimension> dimensions = dimensionQuery.getByScenarioId(scenario.getId());
        List<ScenarioDetailResponse.DimensionResponse> dimensionResponses = dimensions.stream()
                .map(d -> new ScenarioDetailResponse.DimensionResponse(
                        d.getId().value(),
                        d.getName(),
                        d.getDisplayName(),
                        d.getDescription(),
                        d.getConsumerExpectation(),
                        d.getProductInitialOffer()
                ))
                .collect(Collectors.toList());

        // Get Actions with effects
        List<Action> actions = actionQuery.getByScenarioId(scenario.getId());
        List<ScenarioDetailResponse.ActionResponse> actionResponses = actions.stream()
                .map(a -> {
                    List<ActionEffect> effects = actionQuery.getEffectsByActionId(a.getId().value());
                    List<ScenarioDetailResponse.ActionEffectResponse> effectResponses = effects.stream()
                            .map(e -> new ScenarioDetailResponse.ActionEffectResponse(e.dimensionId(), e.delta()))
                            .collect(Collectors.toList());
                    return new ScenarioDetailResponse.ActionResponse(
                            a.getId().value(),
                            a.getName(),
                            a.getDescription(),
                            a.getCost(),
                            a.getCategory().name(),
                            a.isInitiallyLocked(),
                            a.getPrerequisiteActionId(),
                            effectResponses
                    );
                })
                .collect(Collectors.toList());

        // Get Events with effects
        List<GameEvent> events = eventQuery.getByScenarioId(scenario.getId());
        List<ScenarioDetailResponse.EventResponse> eventResponses = events.stream()
                .map(e -> {
                    List<EventEffect> effects = eventQuery.getEffectsByEventId(e.getId().value());
                    List<ScenarioDetailResponse.EventEffectResponse> effectResponses = effects.stream()
                            .map(ef -> new ScenarioDetailResponse.EventEffectResponse(ef.dimensionId(), ef.weightMultiplier()))
                            .collect(Collectors.toList());
                    return new ScenarioDetailResponse.EventResponse(
                            e.getId().value(),
                            e.getTitle(),
                            e.getDescription(),
                            effectResponses
                    );
                })
                .collect(Collectors.toList());

        return new ScenarioDetailResponse(
                scenario.getId(),
                scenario.getTitle(),
                scenario.getDescription(),
                consumerResponse,
                dimensionResponses,
                actionResponses,
                eventResponses
        );
    }

    @Override
    public Page<ScenarioSummaryResponse> getAllPaginated(GetAllScenariosPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return scenarioRepo.findAllPaginated(pageable)
                .map(s -> new ScenarioSummaryResponse(s.getId(), s.getTitle(), s.getDescription()));
    }
}
