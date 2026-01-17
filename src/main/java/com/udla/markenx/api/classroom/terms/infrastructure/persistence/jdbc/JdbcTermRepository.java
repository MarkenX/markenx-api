package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.EnsureAcademicTermExists;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.EnsureAcademicTermIsUpcoming;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTermRepository implements
        TermCommandRepository, EnsureAcademicTermExists, EnsureAcademicTermIsUpcoming {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AcademicTerm> rowMapper = new AcademicTermRowMapper();

    @Override
    public AcademicTerm save(@NonNull AcademicTerm term) {
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

    @Override
    public AcademicTerm findById(String id) {
        try {
            return jdbcTemplate.queryForObject("""
            SELECT *
            FROM academic_terms
            WHERE id = ?
            """,
                rowMapper,
                id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Periodo académico no encontrado: " + id);
        }
    }

    @Override
    public void ensureExists(String id) {
        Boolean exists = jdbcTemplate.queryForObject("""
        SELECT EXISTS (
            SELECT 1
            FROM academic_terms
            WHERE id = ?
        )
        """,
            Boolean.class,
            id
        );

        if (Boolean.FALSE.equals(exists)) {
            throw new EntityNotFoundException("Periodo académico no encontrado: " + id);
        }
    }

    @Override
    public void ensureIsUpcoming(String id) {
        ensureExists(id);

        String status = jdbcTemplate.queryForObject("""
            SELECT status
            FROM academic_terms
            WHERE id = ?
            """,
                String.class,
                id
        );

        if (!TermStatus.UPCOMING.name().equals(status)) {
            throw new EntityNotFoundException("Periodo académico no encontrado: " + id);
        }
    }
}
