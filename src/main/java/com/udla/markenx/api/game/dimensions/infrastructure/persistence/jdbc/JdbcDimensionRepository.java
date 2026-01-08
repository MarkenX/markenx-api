package com.udla.markenx.api.game.dimensions.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.dimensions.domain.ports.outgoing.DimensionCommandRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcDimensionRepository implements DimensionCommandRepository {

    private final DimensionRowMapper rowMapper = new DimensionRowMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dimension save(@NonNull Dimension dimension) {
        jdbcTemplate.update("""
        INSERT INTO dimensions
        (id, name, display_name, description, consumer_expectation, product_initial_offer)
        VALUES (?, ?, ?, ?, ?, ?)
        """,
                dimension.getId().value(),
                dimension.getName(),
                dimension.getDisplayName(),
                dimension.getDescription(),
                dimension.getConsumerExpectation(),
                dimension.getProductInitialOffer()
        );

        return jdbcTemplate.queryForObject("""
                SELECT *
                FROM dimensions
                WHERE id = ?
                """,
                rowMapper,
                dimension.getId().value());
    }

    @Override
    public Dimension findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
            SELECT *
            FROM dimensions
            WHERE id = ?
            """,
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(
                    "No se encontró la dimensión con el identificador: " + id
            );
        }
    }
}
