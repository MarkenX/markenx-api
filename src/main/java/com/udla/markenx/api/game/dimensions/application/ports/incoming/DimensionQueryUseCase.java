package com.udla.markenx.api.game.dimensions.application.ports.incoming;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

import java.util.List;

public interface DimensionQueryUseCase {
    List<Dimension> getAll();
}
