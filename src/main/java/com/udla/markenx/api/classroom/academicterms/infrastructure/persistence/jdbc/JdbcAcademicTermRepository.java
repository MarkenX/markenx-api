package com.udla.markenx.api.classroom.academicterms.infrastructure.persistence.jdbc;

import com.udla.markenx.api.classroom.academicterms.application.exceptions.AcademicTermNotFoundException;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.classroom.academicterms.domain.ports.outgoing.AcademicTermCommandRepository;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.EnsureAcademicTermExists;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.EnsureAcademicTermIsUpcoming;
import com.udla.markenx.api.classroom.courses.domain.exceptions.AcademicTermNotUpcomingException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcAcademicTermRepository implements
        AcademicTermCommandRepository, EnsureAcademicTermExists, EnsureAcademicTermIsUpcoming {

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
            throw new AcademicTermNotFoundException(id);
        }
    }

    @Override
    public void ensureExists(String academicTermId) {
        Boolean exists = jdbcTemplate.queryForObject("""
        SELECT EXISTS (
            SELECT 1
            FROM academic_terms
            WHERE id = ?
        )
        """,
            Boolean.class,
            academicTermId
        );

        if (Boolean.FALSE.equals(exists)) {
            throw new AcademicTermNotFoundException(academicTermId);
        }
    }

    @Override
    public void ensureIsUpcoming(String academicTermId) {
        ensureExists(academicTermId);

        String status = jdbcTemplate.queryForObject("""
            SELECT status
            FROM academic_terms
            WHERE id = ?
            """,
                String.class,
                academicTermId
        );

        if (!AcademicTermStatus.UPCOMING.name().equals(status)) {
            throw new AcademicTermNotUpcomingException(academicTermId);
        }
    }
}
