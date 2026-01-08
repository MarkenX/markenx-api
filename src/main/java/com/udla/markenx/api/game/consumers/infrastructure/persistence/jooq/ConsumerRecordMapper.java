package com.udla.markenx.api.game.consumers.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import org.jooq.Record;

public class ConsumerRecordMapper {

    public Consumer toDomain(Record record) {
        return new Consumer(
                record.get("id", String.class),
                record.get("name", String.class),
                record.get("age", Integer.class),
                record.get("budget", java.math.BigDecimal.class),
                record.get("target_acceptance_score", Double.class)
        );
    }
}
