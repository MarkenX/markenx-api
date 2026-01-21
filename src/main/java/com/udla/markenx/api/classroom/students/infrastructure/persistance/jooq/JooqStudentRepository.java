package com.udla.markenx.api.classroom.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.classroom.students.domain.exceptions.StudentException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqStudentRepository implements StudentQueryRepository {

    private static final String STUDENT_TABLE = "students";
    private static final Field<String> STUDENT_ID_FIELD = field("id", String.class);

    private final DSLContext dsl;
    private final StudentRecordMapper mapper = new StudentRecordMapper();

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(
                dsl.select()
                        .from(STUDENT_TABLE)
                        .where(STUDENT_ID_FIELD.eq(id))
                        .fetchOne(mapper::toDomain));
    }

    @Override
    public Student findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> StudentException.notFoundById(id));
    }

    @Override
    public Page<Student> findAllPaginated(@NonNull Pageable pageable) {
        var records = dsl
                .select()
                .from(STUDENT_TABLE)
                .orderBy(field("last_name").desc())
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch();

        Long total = dsl
                .selectCount()
                .from(STUDENT_TABLE)
                .fetchOneInto(Long.class);

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(
                records.map(mapper::toDomain),
                pageable,
                safeTotal);
    }
}
