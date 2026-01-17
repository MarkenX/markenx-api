package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.FindAllAcademicTermIds;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqTermRepository implements
        TermQueryRepository, FindAllAcademicTermIds {

    private final DSLContext dsl;

    private final AcademicTermRecordMapper mapper =
            new AcademicTermRecordMapper();

    private static final String TERM_TABLE = "academic_terms";
    private static final String TERM_STATUS_FIELD = "status";

    @Override
    public List<AcademicTerm> findAll() {
        return dsl
                .select()
                .from(TERM_TABLE)
                .fetch(mapper::toDomain);
    }

    @Override
    public List<AcademicTerm> findAllByYear(int year) {
        return dsl
                .select()
                .from(TERM_TABLE)
                .where(field("academic_year").eq(year))
                .fetch(mapper::toDomain);
    }

    @Override
    public List<AcademicTerm> findByStatus(@NonNull Set<String> statuses, boolean exclude) {
        if (statuses.isEmpty()) return exclude ? findAll() : List.of();

        var values = statuses.stream().toList();
        var condition = exclude
                ? field(TERM_STATUS_FIELD).notIn(values)
                : field(TERM_STATUS_FIELD).in(values);

        return dsl.select()
                .from(TERM_TABLE)
                .where(condition)
                .fetch(mapper::toDomain);
    }

    @Override
    public AcademicTerm findActiveTerm() {
        return dsl
                .select()
                .from(TERM_TABLE)
                .where(field("status").eq(TermStatus.ACTIVE.name()))
                .fetchOne(mapper::toDomain);
    }

    @Override
    public Page<AcademicTerm> findAllPaginated(@NonNull Pageable pageable) {

        var records = dsl
                .select()
                .from(TERM_TABLE)
                .orderBy(field("academic_year").desc(), field("sequence").desc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch();

        Long total = dsl
                .selectCount()
                .from(TERM_TABLE)
                .fetchOneInto(Long.class);

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(
                records.map(mapper::toDomain),
                pageable,
                safeTotal
        );
    }

    @Override
    public List<String> findAllIds() {
        return dsl
                .select(field("id", String.class))
                .from(TERM_TABLE)
                .fetchInto(String.class);
    }
}
