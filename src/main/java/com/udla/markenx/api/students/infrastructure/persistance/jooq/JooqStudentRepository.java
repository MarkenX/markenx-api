package com.udla.markenx.api.students.infrastructure.persistance.jooq;

import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqStudentRepository implements StudentQueryRepository {

    private final DSLContext dsl;
    private final StudentRecordMapper mapper = new StudentRecordMapper();

    private static final String TABLE = "students";

    @Override
    public Student findById(String id) {
        return null;
    }

    @Override
    public Page<Student> findAllPaginated(@NonNull Pageable pageable) {
        var records = dsl
                .select()
                .from(TABLE)
                .orderBy(field("last_name").desc())
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
}
