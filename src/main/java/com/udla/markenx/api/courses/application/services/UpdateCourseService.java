package com.udla.markenx.api.courses.application.services;

import com.udla.markenx.api.courses.application.ports.incoming.UpdateCourseUseCase;
import com.udla.markenx.api.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCourseService implements UpdateCourseUseCase {

    private final CourseCommandRepository repository;

    @Override
    public Course getById(@NonNull GetCourseByIdQuery query) {
        return repository.findById(query.id());
    }
}
