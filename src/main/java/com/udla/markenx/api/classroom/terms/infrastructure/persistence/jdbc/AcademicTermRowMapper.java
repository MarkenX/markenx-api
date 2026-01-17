package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicTermRowMapper implements RowMapper<AcademicTerm> {

    @Override
    public AcademicTerm mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new AcademicTerm(
                rs.getString("id"),
                LifecycleStatus.valueOf(rs.getString("lifecycle_status")),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getInt("academic_year"),
                rs.getInt("sequence"),
                TermStatus.valueOf(rs.getString("status"))
        );
    }
}