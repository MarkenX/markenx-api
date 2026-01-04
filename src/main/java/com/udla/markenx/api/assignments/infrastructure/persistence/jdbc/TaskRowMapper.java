package com.udla.markenx.api.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {

    /**
     * Maps a row from the {@link ResultSet} to a {@link Task} object.
     *
     * @param rs the {@link ResultSet} containing the current row of data
     * @param rowNum the number of the current row
     * @return a {@link Task} object containing the data from the current row
     * @throws SQLException if an SQL error occurs while accessing the {@link ResultSet}
     */
    @Override
    public Task mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getString("id"),
                LifecycleStatus.valueOf(rs.getString("lifecycle_status")),
                rs.getLong("code"),
                rs.getString("title"),
                rs.getString("summary"),
                rs.getTimestamp("deadline").toLocalDateTime(),
                rs.getDouble("min_score_to_pass"),
                AssignmentStatus.valueOf(rs.getString("status")),
                rs.getString("course_id"),
                rs.getInt("max_attempts"),
                rs.getInt("current_attempt")
        );
    }
}