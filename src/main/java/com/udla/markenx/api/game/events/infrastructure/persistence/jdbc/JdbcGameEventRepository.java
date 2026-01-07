package com.udla.markenx.api.game.events.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import com.udla.markenx.api.game.events.domain.ports.outgoing.GameEventCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcGameEventRepository implements GameEventCommandRepository {

    private final GameEventRowMapper rowMapper = new GameEventRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public GameEvent save(@NonNull GameEvent event) {
        jdbcTemplate.update("""
            INSERT INTO game_events
            (id, title, description)
            VALUES (?, ?, ?)
            """,
                event.getId().value(),
                event.getTitle(),
                event.getDescription()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM game_events
                WHERE id = ?
                """,
                rowMapper,
                event.getId().value());
    }

    @Override
    public GameEvent findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
                SELECT *
                FROM game_events
                WHERE id = ?
                """,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                    "No se encontró el evento con el identificador: " + id
            );
        }
    }

    @Override
    public void saveEffects(String eventId, List<EventEffect> effects) {
        for (EventEffect effect : effects) {
            jdbcTemplate.update("""
                INSERT INTO event_effects
                (id, event_id, dimension_id, weight_multiplier)
                VALUES (?, ?, ?, ?)
                """,
                    effect.id(),
                    eventId,
                    effect.dimensionId(),
                    effect.weightMultiplier()
            );
        }
    }
}
