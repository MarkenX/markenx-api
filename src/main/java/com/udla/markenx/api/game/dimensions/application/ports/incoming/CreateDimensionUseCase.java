package com.udla.markenx.api.game.dimensions.application.ports.incoming;

import com.udla.markenx.api.game.dimensions.application.commands.CreateDimensionCommand;
import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;

public interface CreateDimensionUseCase {
    Dimension handle(CreateDimensionCommand command);
}
