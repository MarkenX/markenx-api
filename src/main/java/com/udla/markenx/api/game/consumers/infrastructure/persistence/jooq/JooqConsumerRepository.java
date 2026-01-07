package com.udla.markenx.api.game.consumers.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.consumers.domain.ports.outgoing.ConsumerQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
@RequiredArgsConstructor
public class JooqConsumerRepository implements ConsumerQueryRepository {

    private static final String TABLE = "consumers";
    private static final String SCENARIOS_TABLE = "scenarios";

    private final DSLContext dsl;
    private final ConsumerRecordMapper mapper = new ConsumerRecordMapper();

    @Override
    public List<Consumer> findAll() {
        return dsl
                .select()
                .from(table(TABLE))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Consumer> findById(String id) {
        return dsl
                .select()
                .from(table(TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Consumer> findByScenarioId(String scenarioId) {
        return dsl
                .select(field("c.*"))
                .from(table(TABLE).as("c"))
                .join(table(SCENARIOS_TABLE).as("s"))
                .on(field("s.consumer_id").eq(field("c.id")))
                .where(field("s.id").eq(scenarioId))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
