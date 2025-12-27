package com.udla.markenx.api.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    /**
     * Maps a row from the {@link ResultSet} to a {@link Student} object.
     *
     * @param rs the {@link ResultSet} containing the current row of data
     * @param rowNum the number of the current row
     * @return a {@link Student} object containing the data from the current row
     * @throws SQLException if an SQL error occurs while accessing the {@link ResultSet}
     */
    @Override
    public Student mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Student(
            rs.getString("id"),
            LifecycleStatus.valueOf(rs.getString("lifecycle_status")),
            rs.getLong("code"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("course_id"),
            rs.getString("user_id")
        );
    }
}
