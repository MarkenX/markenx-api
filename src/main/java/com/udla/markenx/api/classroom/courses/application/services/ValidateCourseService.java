package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.queries.IsActiveCourseQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.ValidateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateCourseService implements ValidateCourseUseCase {

    private final CourseQueryRepository repository;

    @Override
    public boolean isActive(@NonNull IsActiveCourseQuery query) {
        Course course = repository.findByIdOrThrow(query.id());
        return course.isActive();
    }
}
