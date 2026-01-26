package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseRowMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new Course(
                rs.getString("id"),
                LifecycleStatus.valueOf(rs.getString("lifecycle_status")),
                rs.getString("name"),
                rs.getLong("code"),
                rs.getString("academic_term_id")
        );
    }
}
