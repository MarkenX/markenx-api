package com.udla.markenx.api.students.application.ports.incoming;

public interface CourseValidation {
    void ensureCourseExists(String courseId);
}
