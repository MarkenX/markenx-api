package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.mappers.CoursePortMapper;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseStatusQueryCriteria;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import com.udla.markenx.api.shared.application.ports.in.queries.FilterMode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryCourseService implements QueryCourseUseCase {

    private final CourseQueryRepository repository;
    private final CoursePortMapper mapper = new CoursePortMapper();

    @Override
    public CoursePortDTO getCourseById(@NonNull CourseIdQuery query) {
        return mapper.toDTO(repository.findByIdOrThrow(query.id()));
    }

    @Override
    public List<CoursePortDTO> listCourses() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public Page<CoursePortDTO> listCoursesPage(@NonNull CoursePageQueryCriteria criteria) {
        var pageable = PageRequest.of(criteria.page(), criteria.size());
        return repository.findAllPaginated(pageable).map(mapper::toDTO);
    }

    @Override
    public List<CoursePortDTO> listCoursesByStatus(@NonNull CourseStatusQueryCriteria criteria) {
        boolean exclude = criteria.mode() == FilterMode.EXCLUDE;

        return repository.findAllByStatus(criteria.statutes(), exclude).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
