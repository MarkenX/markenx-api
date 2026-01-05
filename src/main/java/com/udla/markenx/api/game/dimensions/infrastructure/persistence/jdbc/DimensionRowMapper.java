package com.udla.markenx.api.game.dimensions.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DimensionRowMapper implements RowMapper<Dimension> {

    /**
     * Maps a row from the {@link ResultSet} to a {@link Dimension} object.
     *
     * @param rs the {@link ResultSet} containing the current row of data
     * @param rowNum the number of the current row
     * @return a {@link Dimension} object containing the data from the current row
     * @throws SQLException if an SQL error occurs while accessing the {@link ResultSet}
     */
    @Override
    public Dimension mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Dimension(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("consumer_expectation"),
                rs.getDouble("product_initial_offer")
        );
    }
}