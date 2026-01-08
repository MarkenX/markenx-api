package com.udla.markenx.api.game.actions.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import com.udla.markenx.api.game.actions.domain.ports.outgoing.ActionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
@RequiredArgsConstructor
public class JooqActionRepository implements ActionQueryRepository {

    private static final String TABLE = "actions";
    private static final String EFFECTS_TABLE = "action_effects";
    private static final String SCENARIO_ACTIONS_TABLE = "scenario_actions";

    private final DSLContext dsl;
    private final ActionRecordMapper mapper = new ActionRecordMapper();

    @Override
    public List<Action> findAll() {
        return dsl
                .select()
                .from(table(TABLE))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Action> findById(String id) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public List<Action> findByScenarioId(String scenarioId) {
        return dsl
                .select()
                .from(table(TABLE).as("a"))
                .join(table(SCENARIO_ACTIONS_TABLE).as("sa"))
                .on(field("sa.action_id").eq(field("a.id")))
                .where(field("sa.scenario_id").eq(scenarioId))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public List<ActionEffect> findEffectsByActionId(String actionId) {
        return dsl
                .select()
                .from(table(EFFECTS_TABLE))
                .where(field("action_id").eq(actionId))
                .fetch()
                .map(mapper::toEffect);
    }
}
