package com.udla.markenx.api.game.events.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import com.udla.markenx.api.game.events.domain.ports.outgoing.GameEventQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
@RequiredArgsConstructor
public class JooqGameEventRepository implements GameEventQueryRepository {

    private static final String TABLE = "game_events";
    private static final String EFFECTS_TABLE = "event_effects";
    private static final String SCENARIO_EVENTS_TABLE = "scenario_events";

    private final DSLContext dsl;
    private final GameEventRecordMapper mapper = new GameEventRecordMapper();

    @Override
    public List<GameEvent> findAll() {
        return dsl
                .select()
                .from(table(TABLE))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<GameEvent> findById(String id) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public List<GameEvent> findByScenarioId(String scenarioId) {
        return dsl
                .select(field("e.*"))
                .from(table(TABLE).as("e"))
                .join(table(SCENARIO_EVENTS_TABLE).as("se"))
                .on(field("se.event_id").eq(field("e.id")))
                .where(field("se.scenario_id").eq(scenarioId))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public List<EventEffect> findEffectsByEventId(String eventId) {
        return dsl
                .select()
                .from(table(EFFECTS_TABLE))
                .where(field("event_id").eq(eventId))
                .fetch()
                .map(mapper::toEffect);
    }
}
