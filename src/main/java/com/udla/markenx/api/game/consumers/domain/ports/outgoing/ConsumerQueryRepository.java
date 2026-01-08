package com.udla.markenx.api.game.consumers.domain.ports.outgoing;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;

import java.util.List;
import java.util.Optional;

public interface ConsumerQueryRepository {
    List<Consumer> findAll();
    Optional<Consumer> findById(String id);
    Optional<Consumer> findByScenarioId(String scenarioId);
}
