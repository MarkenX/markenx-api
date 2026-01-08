package com.udla.markenx.api.game.scenarios.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import com.udla.markenx.api.game.scenarios.domain.ports.outgoing.ScenarioQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
@RequiredArgsConstructor
public class JooqScenarioRepository implements ScenarioQueryRepository {

    private static final String TABLE = "scenarios";
    private static final String SCENARIO_DIMENSIONS_TABLE = "scenario_dimensions";
    private static final String SCENARIO_ACTIONS_TABLE = "scenario_actions";
    private static final String SCENARIO_EVENTS_TABLE = "scenario_events";

    private final DSLContext dsl;
    private final ScenarioRecordMapper mapper = new ScenarioRecordMapper();

    @Override
    public List<Scenario> findAll() {
        return dsl
                .select()
                .from(table(TABLE))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Scenario> findById(String id) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public Page<Scenario> findAllPaginated(Pageable pageable) {
        var records = dsl
                .select()
                .from(table(TABLE))
                .orderBy(field("created_at").desc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch()
                .map(mapper::toDomain);

        Long total = dsl.selectCount().from(table(TABLE)).fetchOneInto(Long.class);
        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(records, pageable, safeTotal);
    }

    @Override
    public List<String> findDimensionIdsByScenarioId(String scenarioId) {
        return dsl
                .select(field("dimension_id"))
                .from(table(SCENARIO_DIMENSIONS_TABLE))
                .where(field("scenario_id").eq(scenarioId))
                .fetch(field("dimension_id"), String.class);
    }

    @Override
    public List<String> findActionIdsByScenarioId(String scenarioId) {
        return dsl
                .select(field("action_id"))
                .from(table(SCENARIO_ACTIONS_TABLE))
                .where(field("scenario_id").eq(scenarioId))
                .fetch(field("action_id"), String.class);
    }

    @Override
    public List<String> findEventIdsByScenarioId(String scenarioId) {
        return dsl
                .select(field("event_id"))
                .from(table(SCENARIO_EVENTS_TABLE))
                .where(field("scenario_id").eq(scenarioId))
                .fetch(field("event_id"), String.class);
    }
}
