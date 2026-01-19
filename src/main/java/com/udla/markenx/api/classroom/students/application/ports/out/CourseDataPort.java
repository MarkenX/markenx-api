package com.udla.markenx.api.classroom.students.application.ports.out;

import java.util.Optional;

/**
 * Anti-Corruption Layer port for accessing course data.
 * This port decouples the students module from the courses module.
 */
public interface CourseDataPort {

    /**
     * Finds course information by course ID.
     *
     * @param courseId the course ID
     * @return course info if found, empty otherwise
     */
    Optional<CourseInfo> findCourseById(String courseId);

    /**
     * Course information record for cross-module communication.
     */
    record CourseInfo(
            String courseId,
            String courseName,
            String term,
            String teacherName
    ) {}
}
