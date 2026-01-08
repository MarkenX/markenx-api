package com.udla.markenx.api.game.actions.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import com.udla.markenx.api.game.actions.domain.ports.outgoing.ActionCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcActionRepository implements ActionCommandRepository {

    private final ActionRowMapper rowMapper = new ActionRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Action save(@NonNull Action action) {
        jdbcTemplate.update("""
            INSERT INTO actions
            (id, name, description, cost, category, is_initially_locked, prerequisite_action_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                action.getId().value(),
                action.getName(),
                action.getDescription(),
                action.getCost(),
                action.getCategory().name(),
                action.isInitiallyLocked(),
                action.getPrerequisiteActionId()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM actions
                WHERE id = ?
                """,
                rowMapper,
                action.getId().value());
    }

    @Override
    public Action findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
                SELECT *
                FROM actions
                WHERE id = ?
                """,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                    "No se encontró la acción con el identificador: " + id
            );
        }
    }

    @Override
    public void saveEffects(String actionId, List<ActionEffect> effects) {
        for (ActionEffect effect : effects) {
            jdbcTemplate.update("""
                INSERT INTO action_effects
                (id, action_id, dimension_id, delta)
                VALUES (?, ?, ?, ?)
                """,
                    effect.id(),
                    actionId,
                    effect.dimensionId(),
                    effect.delta()
            );
        }
    }
}
