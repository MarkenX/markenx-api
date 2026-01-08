package com.udla.markenx.api.game.actions.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.actions.domain.models.aggregates.Action;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionCategory;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActionRowMapper implements RowMapper<Action> {

    @Override
    public Action mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Action(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("cost"),
                ActionCategory.valueOf(rs.getString("category")),
                rs.getBoolean("is_initially_locked"),
                rs.getString("prerequisite_action_id"),
                new ArrayList<>()
        );
    }
}
