package com.udla.markenx.api.game.actions.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionCategory;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ActionRecordMapper {

    public Action toDomain(Record record) {
        return new Action(
                record.get("id", String.class),
                record.get("name", String.class),
                record.get("description", String.class),
                record.get("cost", BigDecimal.class),
                ActionCategory.valueOf(record.get("category", String.class)),
                record.get("is_initially_locked", Boolean.class),
                record.get("prerequisite_action_id", String.class),
                new ArrayList<>()
        );
    }

    public ActionEffect toEffect(Record record) {
        return new ActionEffect(
                record.get("id", String.class),
                record.get("dimension_id", String.class),
                record.get("delta", Double.class)
        );
    }
}
