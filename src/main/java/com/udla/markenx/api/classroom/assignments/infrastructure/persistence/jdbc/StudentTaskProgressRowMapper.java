package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps JDBC result rows to StudentTaskProgress domain objects.
 */
public class StudentTaskProgressRowMapper implements RowMapper<StudentTaskProgress> {

    @Override
    public StudentTaskProgress mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new StudentTaskProgress(
                rs.getString("student_id"),
                rs.getString("task_id"),
                rs.getInt("current_attempt"),
                AssignmentStatus.valueOf(rs.getString("status"))
        );
    }
}
