package com.udla.markenx.api.game.events.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.events.domain.models.aggregates.GameEvent;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameEventRowMapper implements RowMapper<GameEvent> {

    @Override
    public GameEvent mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new GameEvent(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("description"),
                new ArrayList<>()
        );
    }
}
