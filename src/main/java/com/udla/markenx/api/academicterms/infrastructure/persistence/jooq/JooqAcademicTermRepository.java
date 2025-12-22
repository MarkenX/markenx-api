package com.udla.markenx.api.academicterms.infrastructure.persistence.jooq;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermQueryRepository;
import com.udla.markenx.api.courses.application.ports.incoming.FindAllAcademicTermIds;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqAcademicTermRepository implements
        AcademicTermQueryRepository, FindAllAcademicTermIds {

    private final DSLContext dsl;

    private final AcademicTermRecordMapper mapper =
            new AcademicTermRecordMapper();

    private static final String TABLE = "academic_terms";

    @Override
    public List<AcademicTerm> findAll() {
        return dsl
                .select()
                .from(TABLE)
                .fetch(mapper::toDomain);
    }

    @Override
    public List<AcademicTerm> findAllByYear(int year) {
        return dsl
                .select()
                .from(TABLE)
                .where(field("academic_year").eq(year))
                .fetch(mapper::toDomain);
    }

    @Override
    public List<AcademicTerm> findByStatusNot(@NonNull AcademicTermStatus status) {
        return dsl
                .select()
                .from(TABLE)
                .where(field("status").ne(status.name()))
                .fetch(mapper::toDomain);
    }

    @Override
    public Page<AcademicTerm> findAllPaginated(@NonNull Pageable pageable) {

        var records = dsl
                .select()
                .from(TABLE)
                .orderBy(field("academic_year").desc(), field("sequence").desc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch();

        Long total = dsl
                .selectCount()
                .from(TABLE)
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
                .from(TABLE)
                .fetchInto(String.class);
    }
}
