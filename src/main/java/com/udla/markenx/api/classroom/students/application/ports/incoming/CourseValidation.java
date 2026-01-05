package com.udla.markenx.api.classroom.students.application.ports.incoming;

public interface CourseValidation {
    void ensureCourseExists(String courseId);
}
