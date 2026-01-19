package com.udla.markenx.api.classroom.courses.infrastructure.adapters;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import com.udla.markenx.api.classroom.students.application.ports.out.CourseDataPort;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseDataAdapter implements CourseDataPort {

    private final QueryTermsUseCase queryTermsUseCase;
    private final CourseCommandRepository courseRepository;

    @Override
    public @NonNull Optional<CourseInfo> findCourseById(@NonNull String courseId) {
        try {
            Course course = courseRepository.findById(courseId);

            var query = new TermIdQuery(course.getTermId());
            TermPortDTO term = queryTermsUseCase.getTermById(query);

            return Optional.of(new CourseInfo(
                    course.getId().value(),
                    course.getName(),
                    term.label()
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
