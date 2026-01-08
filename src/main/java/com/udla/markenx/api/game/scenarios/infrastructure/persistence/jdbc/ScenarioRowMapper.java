package com.udla.markenx.api.game.scenarios.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.scenarios.domain.models.aggregates.Scenario;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScenarioRowMapper implements RowMapper<Scenario> {

    @Override
    public Scenario mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Scenario(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("consumer_id"),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
