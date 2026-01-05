package com.udla.markenx.api.game.dimensions.domain.ports.outgoing;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

import java.util.List;

public interface DimensionQueryRepository {
    List<Dimension> findAll();
}
