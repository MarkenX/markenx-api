package com.udla.markenx.api.game.events.domain.ports.outgoing;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;

import java.util.List;

public interface GameEventCommandRepository {
    GameEvent save(GameEvent event);
    GameEvent findById(String id);
    void saveEffects(String eventId, List<EventEffect> effects);
}
