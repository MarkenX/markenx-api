package com.udla.markenx.api.game.dimensions.application.ports.incoming;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

import java.util.List;
import java.util.Optional;

public interface DimensionQueryUseCase {
    List<Dimension> getAll();
    Optional<Dimension> getById(String id);
    List<Dimension> getByScenarioId(String scenarioId);
}
