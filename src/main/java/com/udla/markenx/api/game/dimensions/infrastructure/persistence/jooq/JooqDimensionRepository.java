package com.udla.markenx.api.game.dimensions.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.dimensions.domain.ports.outgoing.DimensionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
@RequiredArgsConstructor
public class JooqDimensionRepository implements DimensionQueryRepository {

    private static final String TABLE = "dimensions";
    private static final String SCENARIO_DIMENSIONS_TABLE = "scenario_dimensions";

    private final DSLContext dsl;
    private final DimensionRecordMapper mapper = new DimensionRecordMapper();

    @Override
    public List<Dimension> findAll() {
        return dsl
                .select()
                .from(table(TABLE))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Dimension> findById(String id) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public List<Dimension> findByScenarioId(String scenarioId) {
        return dsl
                .select()
                .from(table(TABLE).as("d"))
                .join(table(SCENARIO_DIMENSIONS_TABLE).as("sd"))
                .on(field("sd.dimension_id").eq(field("d.id")))
                .where(field("sd.scenario_id").eq(scenarioId))
                .fetch()
                .map(mapper::toDomain);
    }

}
