package com.udla.markenx.api.game.attempts.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.domain.models.entities.TurnHistory;
import com.udla.markenx.api.game.attempts.domain.ports.outgoing.AttemptCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcAttemptRepository implements AttemptCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Attempt save(Attempt attempt) {
        jdbcTemplate.update("""
            INSERT INTO attempts
            (id, task_id, student_id, status, session_date, final_acceptance,
             remaining_budget, total_turns_used, profile_discovery_percentage, final_outcome)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            attempt.getId(),
            attempt.getTaskId(),
            attempt.getStudentId(),
            attempt.getStatus().name(),
            Timestamp.valueOf(attempt.getSessionDate()),
            attempt.getResult().approvalRate(),
            attempt.getResult().budgetRemaining(),
            attempt.getResult().currentTurn(),
            attempt.getResult().profileScore(),
            attempt.getStatus().name()
        );

        return attempt;
    }

    @Override
    public void saveTurnHistories(String attemptId, List<TurnHistory> turnHistories) {
        for (TurnHistory turnHistory : turnHistories) {
            jdbcTemplate.update("""
                INSERT INTO turn_histories
                (id, attempt_id, turn_number, acceptance_at_end, budget_at_end, event_occurred_title)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                turnHistory.getId(),
                attemptId,
                turnHistory.getTurnNumber(),
                turnHistory.getAcceptanceAtEnd(),
                turnHistory.getBudgetAtEnd(),
                turnHistory.getEventOccurredTitle()
            );
        }
    }

    @Override
    public void saveTurnActions(String turnHistoryId, List<String> actionIds) {
        for (String actionId : actionIds) {
            jdbcTemplate.update("""
                INSERT INTO turn_actions (id, turn_history_id, action_id)
                VALUES (?, ?, ?)
                """,
                UUID.randomUUID().toString(),
                turnHistoryId,
                actionId
            );
        }
    }
}
