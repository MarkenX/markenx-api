package com.udla.markenx.api.game.scenarios.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import com.udla.markenx.api.game.scenarios.domain.ports.outgoing.ScenarioCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcScenarioRepository implements ScenarioCommandRepository {

    private final ScenarioRowMapper rowMapper = new ScenarioRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Scenario save(@NonNull Scenario scenario) {
        jdbcTemplate.update("""
            INSERT INTO scenarios
            (id, title, description)
            VALUES (?, ?, ?)
            """,
                scenario.getId(),
                scenario.getTitle(),
                scenario.getDescription()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM scenarios
                WHERE id = ?
                """,
                rowMapper,
                scenario.getId());
    }

    @Override
    public Scenario findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
                SELECT *
                FROM scenarios
                WHERE id = ?
                """,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                    "No se encontró el escenario con el identificador: " + id
            );
        }
    }

    @Override
    public void associateConsumer(String scenarioId, String consumerId) {
        jdbcTemplate.update("""
            UPDATE scenarios
            SET consumer_id = ?
            WHERE id = ?
            """,
                consumerId,
                scenarioId
        );
    }

    @Override
    public void addDimensions(String scenarioId, List<String> dimensionIds) {
        for (String dimensionId : dimensionIds) {
            jdbcTemplate.update("""
                INSERT INTO scenario_dimensions
                (scenario_id, dimension_id)
                VALUES (?, ?)
                """,
                    scenarioId,
                    dimensionId
            );
        }
    }

    @Override
    public void addActions(String scenarioId, List<String> actionIds) {
        for (String actionId : actionIds) {
            jdbcTemplate.update("""
                INSERT INTO scenario_actions
                (scenario_id, action_id)
                VALUES (?, ?)
                """,
                    scenarioId,
                    actionId
            );
        }
    }

    @Override
    public void addEvents(String scenarioId, List<String> eventIds) {
        for (String eventId : eventIds) {
            jdbcTemplate.update("""
                INSERT INTO scenario_events
                (scenario_id, event_id)
                VALUES (?, ?)
                """,
                    scenarioId,
                    eventId
            );
        }
    }
}
