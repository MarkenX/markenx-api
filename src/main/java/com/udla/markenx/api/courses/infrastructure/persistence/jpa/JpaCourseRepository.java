package com.udla.markenx.api.courses.infrastructure.persistence.jpa;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseRepository;
import com.udla.markenx.api.courses.infrastructure.mappers.CourseJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCourseRepository implements CourseRepository {

    private final SpringDataCourseRepository springRepo;
    private final CourseJpaMapper mapper;

    @Override
    public Page<Course> findAllPaginated(Pageable pageable) {
        return springRepo.findAll(pageable).map(mapper::toDomain);
    }
}
