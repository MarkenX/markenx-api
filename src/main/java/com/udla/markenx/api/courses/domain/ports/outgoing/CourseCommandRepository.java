package com.udla.markenx.api.courses.domain.ports.outgoing;

import com.udla.markenx.api.courses.domain.models.aggregates.Course;

public interface CourseCommandRepository {
    Course save(Course course);
}
