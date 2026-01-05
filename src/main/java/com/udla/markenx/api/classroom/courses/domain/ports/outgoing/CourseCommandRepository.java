package com.udla.markenx.api.classroom.courses.domain.ports.outgoing;

import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface CourseCommandRepository {
    Course save(Course course);
    Course findById(String id);
}
