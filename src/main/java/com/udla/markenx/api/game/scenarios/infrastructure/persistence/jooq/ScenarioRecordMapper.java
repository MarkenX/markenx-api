package com.udla.markenx.api.game.scenarios.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import org.jooq.Record;

import java.util.ArrayList;

public class ScenarioRecordMapper {

    public Scenario toDomain(Record record) {
        return new Scenario(
                record.get("id", String.class),
                record.get("title", String.class),
                record.get("description", String.class),
                record.get("consumer_id", String.class),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
