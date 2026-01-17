package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CourseQueryUseCase;
import com.udla.markenx.api.classroom.courses.application.queries.GetAllCoursesPaginatedQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseQueryService implements CourseQueryUseCase {

    private final CourseQueryRepository repository;

    @Override
    public Page<Course> getAllPaginated(@NonNull GetAllCoursesPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }
}
