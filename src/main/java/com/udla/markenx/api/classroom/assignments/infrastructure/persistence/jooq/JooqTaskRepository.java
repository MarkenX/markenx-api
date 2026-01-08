package com.udla.markenx.api.classroom.assignments.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.classroom.assignments.domain.ports.outgoing.TaskQueryRepository;
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
public class JooqTaskRepository implements TaskQueryRepository {

    private final DSLContext dsl;
    private final TaskRecordMapper mapper = new TaskRecordMapper();

    private static final String TABLE = "tasks";

    @Override
    public List<Task> findAll() {
        return dsl
                .select()
                .from(TABLE)
                .fetch(mapper::toDomain);
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
    public List<Task> findByStatuses(@NonNull List<AssignmentStatus> statuses) {
        var statusNames = statuses.stream()
                .map(AssignmentStatus::name)
                .toList();

        return dsl
                .select()
                .from(TABLE)
                .where(field("status").in(statusNames))
                .fetch(mapper::toDomain);
    }
}
