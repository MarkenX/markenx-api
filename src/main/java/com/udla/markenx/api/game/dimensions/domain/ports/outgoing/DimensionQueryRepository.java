package com.udla.markenx.api.game.dimensions.domain.ports.outgoing;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

import java.util.List;
import java.util.Optional;

public interface DimensionQueryRepository {
    List<Dimension> findAll();
    Optional<Dimension> findById(String id);
    List<Dimension> findByScenarioId(String scenarioId);
}
