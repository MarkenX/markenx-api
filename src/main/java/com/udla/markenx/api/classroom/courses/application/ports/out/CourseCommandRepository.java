package com.udla.markenx.api.classroom.courses.application.ports.out;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface CourseCommandRepository {
    Course save(Course course);
}
