package com.udla.markenx.api.game.attempts.infrastructure.adapters;

import com.udla.markenx.api.game.attempts.application.ports.incoming.TaskScoreProvider;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.*;

@Component
@RequiredArgsConstructor
public class TaskScoreAdapter implements TaskScoreProvider {

    private static final String TASKS_TABLE = "tasks";

    private final DSLContext dsl;

    @Override
    public double getMinScoreToPass(String taskId) {
        Double score = dsl
                .select(field("min_score_to_pass"))
                .from(table(TASKS_TABLE))
                .where(field("id").eq(taskId))
                .fetchOneInto(Double.class);

        if (score == null) {
            throw new IllegalArgumentException("Task not found with id: " + taskId);
        }

        return score;
    }
}
