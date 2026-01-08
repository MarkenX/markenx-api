package com.udla.markenx.api.game.attempts.infrastructure.persistence.jooq;

import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import com.udla.markenx.api.game.attempts.domain.ports.outgoing.AttemptQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class JooqAttemptRepository implements AttemptQueryRepository {

    private static final String ATTEMPTS_TABLE = "attempts";
    private static final String TURN_HISTORIES_TABLE = "turn_histories";
    private static final String TURN_ACTIONS_TABLE = "turn_actions";

    private final DSLContext dsl;

    @Override
    public Optional<Attempt> findById(String id) {
        return dsl
                .select()
                .from(table(ATTEMPTS_TABLE))
                .where(field("id").eq(id))
                .fetchOptional()
                .map(this::mapToAttempt);
    }

    @Override
    public List<TurnHistory> findTurnHistoriesByAttemptId(String attemptId) {
        return dsl
                .select()
                .from(table(TURN_HISTORIES_TABLE))
                .where(field("attempt_id").eq(attemptId))
                .orderBy(field("turn_number").asc())
                .fetch()
                .map(this::mapToTurnHistory);
    }

    @Override
    public List<String> findActionIdsByTurnHistoryId(String turnHistoryId) {
        return dsl
                .select(field("action_id"))
                .from(table(TURN_ACTIONS_TABLE))
                .where(field("turn_history_id").eq(turnHistoryId))
                .fetch(field("action_id"), String.class);
    }

    private Attempt mapToAttempt(Record record) {
        return new Attempt(
                record.get("id", String.class),
                record.get("total_turns_used", Integer.class),
                record.get("remaining_budget", BigDecimal.class),
                record.get("final_acceptance", Double.class),
                record.get("profile_discovery_percentage", Double.class),
                AttemptStatus.valueOf(record.get("status", String.class)),
                record.get("task_id", String.class),
                record.get("student_id", String.class),
                record.get("session_date", LocalDateTime.class),
                null // Turn histories loaded separately
        );
    }

    private TurnHistory mapToTurnHistory(Record record) {
        return new TurnHistory(
                record.get("id", String.class),
                record.get("turn_number", Integer.class),
                record.get("acceptance_at_end", Double.class),
                record.get("budget_at_end", BigDecimal.class),
                record.get("event_occurred_title", String.class),
                null // Actions loaded separately
        );
    }
}
