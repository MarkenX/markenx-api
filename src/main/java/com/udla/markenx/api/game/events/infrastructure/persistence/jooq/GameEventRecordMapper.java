package com.udla.markenx.api.game.events.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import org.jooq.Record;

import java.util.ArrayList;

public class GameEventRecordMapper {

    public GameEvent toDomain(Record record) {
        return new GameEvent(
                record.get("id", String.class),
                record.get("title", String.class),
                record.get("description", String.class),
                new ArrayList<>()
        );
    }

    public EventEffect toEffect(Record record) {
        return new EventEffect(
                record.get("id", String.class),
                record.get("dimension_id", String.class),
                record.get("weight_multiplier", Double.class)
        );
    }
}
