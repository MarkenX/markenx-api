package com.udla.markenx.api.classroom.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadQueryRepository;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class JooqStudentSummaryReadQueryRepository implements StudentSummaryReadQueryRepository {

    private static final Table<?> STUDENT_SUMMARY =
            table(name("student_summary_read_model"));

    private static final Field<String> STUDENT_ID =
            field(name("student_id"), String.class);

    private static final Field<String> EMAIL =
            field(name("email"), String.class);

    private static final Field<String> FULL_NAME =
            field(name("full_name"), String.class);

    private final DSLContext dsl;

    private final StudentUserRecordMapper mapper = new StudentUserRecordMapper();

    public JooqStudentSummaryReadQueryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<StudentSummaryReadModel> findById(String id) {
        return Optional.ofNullable(
                dsl.select()
                        .from(STUDENT_SUMMARY)
                        .where(STUDENT_ID.eq(id))
                        .fetchOne(mapper::toDomain));
    }

    @Override
    public StudentSummaryReadModel findByEmail(String email) {
        return Optional.ofNullable(
                dsl.select()
                        .from(STUDENT_SUMMARY)
                        .where(EMAIL.eq(email))
                        .fetchOne(mapper::toDomain)
        ).orElseThrow(() -> new EntityNotFoundException("Student", "email", email));
    }

    @Override
    public List<StudentSummaryReadModel> findAll() {
        return dsl.select()
                .from(STUDENT_SUMMARY)
                .fetch(mapper::toDomain);
    }

    @Override
    public StudentSummaryReadModel findByIdOrThrow(String id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StudentUserReadDTO.class.getName(), "id", id));
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
