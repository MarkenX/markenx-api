package com.udla.markenx.api.game.consumers.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.consumers.domain.ports.outgoing.ConsumerCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcConsumerRepository implements ConsumerCommandRepository {

    private final ConsumerRowMapper rowMapper = new ConsumerRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Consumer save(@NonNull Consumer consumer) {
        jdbcTemplate.update("""
            INSERT INTO consumers
            (id, name, age, budget, target_acceptance_score)
            VALUES (?, ?, ?, ?, ?)
            """,
                consumer.getId().value(),
                consumer.getName(),
                consumer.getAge(),
                consumer.getBudget(),
                consumer.getTargetAcceptanceScore()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM consumers
                WHERE id = ?
                """,
                rowMapper,
                consumer.getId().value());
    }

    @Override
    public Consumer findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
                SELECT *
                FROM consumers
                WHERE id = ?
                """,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                    "No se encontró el consumidor con el identificador: " + id
            );
        }
    }
}
