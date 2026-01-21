package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.AssignmentException;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
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
public class JooqTaskRepository implements TaskQueryRepository {

    private static final String TABLE = "tasks";
    private static final Field<String> TERM_ID_FIELD = field("id", String.class);
    private static final Field<String> TASK_STATUS_FIELD = field("status", String.class);

    private final DSLContext dsl;
    private final TaskRecordMapper mapper = new TaskRecordMapper();

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(
                dsl.select()
                        .from(TABLE)
                        .where(TERM_ID_FIELD.eq(id))
                        .fetchOne(mapper::toDomain)
        );
    }

    @Override
    public Task findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() -> AssignmentException.notFoundById(id));
    }

    @Override
    public List<Task> findAll() {
        return Optional.of(dsl
                        .select()
                        .from(TABLE)
                        .fetch(mapper::toDomain))
                .orElseThrow(AssignmentException::noneFound);
    }

    @Override
    public Page<Task> findAllPaginated(@NonNull Pageable pageable) {
        var records = dsl
                .select()
                .from(TABLE)
                .orderBy(field("title").desc())
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
    public List<Task> findByStatuses(@NonNull Set<String> statuses, boolean exclude) {
        if (statuses.isEmpty())
            return exclude ? findAll() : List.of();

        var values = statuses.stream().toList();
        var condition = exclude
                ? TASK_STATUS_FIELD.notIn(values)
                : TASK_STATUS_FIELD.in(values);

        return Optional.of(dsl.select()
                .from(TABLE)
                .where(condition)
                .fetch(mapper::toDomain))
                .orElseThrow(() -> AssignmentException.noneFoundByStatuses(statuses));
    }

    @Override
    public List<Task> findByCourseId(String courseId) {
        return dsl
                .select()
                .from(TABLE)
                .where(field("course_id").eq(courseId))
                .orderBy(field("deadline").asc())
                .fetch(mapper::toDomain);
    }
}
