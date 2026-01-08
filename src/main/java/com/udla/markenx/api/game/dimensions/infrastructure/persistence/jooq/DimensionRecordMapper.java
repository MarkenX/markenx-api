package com.udla.markenx.api.game.dimensions.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import org.jooq.Record;

public class DimensionRecordMapper {

    public Dimension toDomain(Record record) {
        return new Dimension(
                record.get("id", String.class),
                record.get("name", String.class),
                record.get("display_name", String.class),
                record.get("description", String.class),
                record.get("consumer_expectation", Double.class),
                record.get("product_initial_offer", Double.class)
        );
    }
}
