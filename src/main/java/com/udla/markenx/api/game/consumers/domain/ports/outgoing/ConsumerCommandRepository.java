package com.udla.markenx.api.game.consumers.domain.ports.outgoing;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;

public interface ConsumerCommandRepository {
    Consumer save(Consumer consumer);
    Consumer findById(String id);
}
