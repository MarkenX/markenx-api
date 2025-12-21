package com.udla.markenx.api.academicterms.infrastructure.persistence.jdbc;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcAcademicTermRepository implements AcademicTermCommandRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AcademicTerm> rowMapper = new AcademicTermRowMapper();

    @Override
    public AcademicTerm save(@NonNull AcademicTerm term) {
        jdbcTemplate.update("""
            INSERT INTO academic_terms
            (id, lifecycle_status, start_date, end_date, academic_year, sequence, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
                term.getId().value(),
                term.getLifecycleStatus().name(),
                term.getStartDate(),
                term.getEndDate(),
                term.getYear(),
                term.getSequence(),
                term.getStatus().name()
        );
        return term;
    }

    @Override
    public AcademicTerm findById(String id) {
        return jdbcTemplate.queryForObject("""
            SELECT *
            FROM academic_terms
            WHERE id = ?
            """,
                rowMapper,
                id
        );
    }
}
