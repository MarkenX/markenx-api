package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.ListCoursesUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListCoursesService implements ListCoursesUseCase {

    private final CourseQueryRepository repository;

    @Override
    public Page<Course> getAllPaginated(@NonNull CoursePageQueryCriteria query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }
}
