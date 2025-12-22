package com.udla.markenx.api.courses.application.services;

import com.udla.markenx.api.courses.application.ports.incoming.CourseQueryUseCase;
import com.udla.markenx.api.courses.application.queries.GetAllCoursesPaginatedQuery;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseQueryRepository;
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
