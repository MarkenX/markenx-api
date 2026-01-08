package com.udla.markenx.api.game.consumers.application.ports.incoming;

import com.udla.markenx.api.game.consumers.application.commands.CreateConsumerCommand;
import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;

public interface CreateConsumerUseCase {
    Consumer handle(CreateConsumerCommand command);
}
