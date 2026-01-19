package com.udla.markenx.api.classroom.courses.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.FindAllCoursesIdsForAssignmentsHandler;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.FindAllCoursesIdsForStudentsHandler;
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

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class JooqCourseRepository implements CourseQueryRepository,
        FindAllCoursesIdsForStudentsHandler,
        FindAllCoursesIdsForAssignmentsHandler {

    private static final Field<String> COURSE_ID_FIELD = field("id", String.class);

    private final DSLContext dsl;
    private final CourseRecordMapper mapper = new CourseRecordMapper();

    private static final String TABLE = "courses";

    @Override
    public Optional<Course> findById(String id) {
        return Optional.ofNullable(
                dsl.select()
                        .from(TABLE)
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
    public List<String> handle() {
        return dsl
                .select(field("id", String.class))
                .from(TABLE)
                .fetchInto(String.class);
    }
}
