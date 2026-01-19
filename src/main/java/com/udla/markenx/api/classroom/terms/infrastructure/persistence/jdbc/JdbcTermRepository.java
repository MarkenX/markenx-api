package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTermRepository implements TermCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Term save(@NonNull Term term) {
        jdbcTemplate.update("""
        INSERT INTO academic_terms
        (id, lifecycle_status, start_date, end_date, academic_year, sequence, status)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            lifecycle_status = VALUES(lifecycle_status),
            start_date      = VALUES(start_date),
            end_date        = VALUES(end_date),
            academic_year   = VALUES(academic_year),
            sequence        = VALUES(sequence),
            status          = VALUES(status)
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
}
