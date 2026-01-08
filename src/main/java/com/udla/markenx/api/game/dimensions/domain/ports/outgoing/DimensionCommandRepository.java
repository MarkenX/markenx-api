package com.udla.markenx.api.game.dimensions.domain.ports.outgoing;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

public interface DimensionCommandRepository {
    Dimension save(Dimension task);
    Dimension findById(String id);
}
