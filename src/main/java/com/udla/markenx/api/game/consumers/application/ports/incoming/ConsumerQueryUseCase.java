package com.udla.markenx.api.game.consumers.application.ports.incoming;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;

import java.util.List;
import java.util.Optional;

public interface ConsumerQueryUseCase {
    List<Consumer> getAll();
    Optional<Consumer> getById(String id);
    Optional<Consumer> getByScenarioId(String scenarioId);
}
