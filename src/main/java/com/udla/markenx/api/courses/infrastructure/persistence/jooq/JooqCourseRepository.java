package com.udla.markenx.api.courses.infrastructure.persistence.jooq;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseQueryRepository;
import com.udla.markenx.api.students.application.ports.incoming.FindAllCoursesIds;
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
public class JooqCourseRepository
        implements CourseQueryRepository, FindAllCoursesIds {

    private final DSLContext dsl;
    private final CourseRecordMapper mapper = new CourseRecordMapper();

    private static final String TABLE = "courses";

    @Override
    public Page<Course> findAllPaginated(@NonNull Pageable pageable) {
        var records = dsl
                .select()
                .from(TABLE)
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
