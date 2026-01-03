package com.udla.markenx.api.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.students.query.repositories.StudentSummaryPagedReadRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

@Repository
public class JooqStudentSummaryPagedReadRepository
        implements StudentSummaryPagedReadRepository {

    private static final Table<?> STUDENT_SUMMARY =
            table(name("student_summary_read_model"));

    private static final Field<String> STUDENT_ID =
            field(name("student_id"), String.class);

    private static final Field<String> EMAIL =
            field(name("email"), String.class);

    private static final Field<String> FULL_NAME =
            field(name("full_name"), String.class);

    private final DSLContext dsl;

    public JooqStudentSummaryPagedReadRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Page<StudentSummaryReadModel> findAllPaginated(
            @NonNull Pageable pageable
    ) {

        int total = dsl.fetchCount(STUDENT_SUMMARY);

        var records = dsl
                .select(STUDENT_ID, EMAIL, FULL_NAME)
                .from(STUDENT_SUMMARY)
                .orderBy(FULL_NAME.asc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch();

        var content = records.map(record ->
                new StudentSummaryReadModel(
                        record.get(STUDENT_ID),
                        record.get(EMAIL),
                        record.get(FULL_NAME)
                )
        );

        return new PageImpl<>(content, pageable, total);
    }
}
