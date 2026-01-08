package com.udla.markenx.api.game.consumers.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsumerRowMapper implements RowMapper<Consumer> {

    @Override
    public Consumer mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Consumer(
                rs.getString("id"),
                rs.getString("name"),
                rs.getObject("age", Integer.class),
                rs.getBigDecimal("budget"),
                rs.getDouble("target_acceptance_score")
        );
    }
}
