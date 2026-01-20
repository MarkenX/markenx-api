package com.udla.markenx.api.classroom.terms.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqTermRepository implements TermQueryRepository {

        private static final String TERM_TABLE = "academic_terms";
        private static final Field<String> TERM_ID_FIELD = field("id", String.class);
        private static final Field<String> TERM_STATUS_FIELD = field("status", String.class);
        private static final Field<Integer> TERM_SEQUENCE_FIELD = field("sequence", Integer.class);
        private static final Field<Integer> TERM_YEAR_FIELD = field("academic_year", Integer.class);

        private final DSLContext dsl;
        private final AcademicTermRecordMapper mapper = new AcademicTermRecordMapper();

        @Override
        public Optional<Term> findById(@NonNull String id) {
                return Optional.ofNullable(
                                dsl.select()
                                                .from(TERM_TABLE)
                                                .where(TERM_ID_FIELD.eq(id))
                                                .fetchOne(mapper::toDomain));
        }

        @Override
        public Optional<Term> findActiveTerm() {
                return Optional.ofNullable(
                                dsl.select()
                                                .from(TERM_TABLE)
                                                .where(TERM_STATUS_FIELD.eq(TermStatus.ACTIVE.name()))
                                                .fetchOne(mapper::toDomain));
        }

        @Override
        public Term findByIdOrThrow(@NonNull String id) {
                return findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(Term.class.getName(), id));
        }

        @Override
        public List<Term> findAll() {
                return dsl
                                .select()
                                .from(TERM_TABLE)
                                .fetch(mapper::toDomain);
        }

        @Override
        public List<Term> findAllByStatus(@NonNull Set<String> statuses, boolean exclude) {
                if (statuses.isEmpty())
                        return exclude ? findAll() : List.of();

                var values = statuses.stream().toList();
                var condition = exclude
                                ? TERM_STATUS_FIELD.notIn(values)
                                : TERM_STATUS_FIELD.in(values);

                return dsl.select()
                                .from(TERM_TABLE)
                                .where(condition)
                                .fetch(mapper::toDomain);
        }

        @Override
        public List<Term> findAllByYear(int year) {
                return dsl
                                .select()
                                .from(TERM_TABLE)
                                .where(TERM_YEAR_FIELD.eq(year))
                                .fetch(mapper::toDomain);
        }

        @Override
        public Page<Term> findAllPaginated(@NonNull Pageable pageable) {

                var records = dsl
                                .select()
                                .from(TERM_TABLE)
                                .orderBy(TERM_YEAR_FIELD.desc(), TERM_SEQUENCE_FIELD.desc())
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
                                safeTotal);
        }
}
