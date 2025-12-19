package com.udla.markenx.api.courses.infrastructure.mappers;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.infrastructure.persistence.jpa.CourseJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CourseJpaMapper {

    public Course toDomain(CourseJpaEntity entity) {
        return new Course(
                entity.getId(),
                entity.getName(),
                entity.getInternalCode(),
                entity.getAcademicTerm().getId()
        );
    }

    public CourseJpaEntity toEntity(Course domain) {
        return new CourseJpaEntity(
                domain.getId().toString(),
                domain.getLifecycleStatus(),
                domain.getName()
        );
    }
}
