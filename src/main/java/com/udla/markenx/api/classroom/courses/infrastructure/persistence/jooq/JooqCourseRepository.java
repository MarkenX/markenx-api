package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
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
public class JooqCourseRepository implements CourseQueryRepository {

    private static final String COURSE_TABLE = "courses";
    private static final Field<String> COURSE_ID_FIELD = field("id", String.class);
    private static final Field<String> COURSE_STATUS_FIELD = field("status", String.class);

    private final DSLContext dsl;
    private final CourseRecordMapper mapper = new CourseRecordMapper();

    @Override
    public Optional<Course> findById(String id) {
        return Optional.ofNullable(
                dsl.select()
                        .from(COURSE_TABLE)
                        .where(COURSE_ID_FIELD.eq(id))
                        .fetchOne(mapper::toDomain)
        );
    }

    @Override
    public Course findByIdOrThrow(String id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Course.class.getName(), id));
    }

    @Override
    public List<Course> findAll() {
        return dsl
                .select()
                .from(COURSE_TABLE)
                .fetch(mapper::toDomain);
    }

    @Override
    public List<Course> findAllByStatus(@NonNull Set<String> statuses, boolean exclude) {
        if (statuses.isEmpty()) return exclude ? findAll() : List.of();

        var values = statuses.stream().toList();
        var condition = exclude
                ? COURSE_STATUS_FIELD.notIn(values)
                : COURSE_STATUS_FIELD.in(values);

        return dsl.select()
                .from(COURSE_TABLE)
                .where(condition)
                .fetch(mapper::toDomain);
    }

    @Override
    public Page<Course> findAllPaginated(@NonNull Pageable pageable) {
        var records = dsl
                .select()
                .from(COURSE_TABLE)
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset())
                .fetch();

        Long total = dsl
                .selectCount()
                .from(COURSE_TABLE)
                .fetchOneInto(Long.class);

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(
                records.map(mapper::toDomain),
                pageable,
                safeTotal
        );
    }
}
