package com.udla.markenx.api.classroom.courses.infrastructure.adapters;

import com.udla.markenx.api.classroom.academicterms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.classroom.courses.domain.ports.outgoing.CourseCommandRepository;
import com.udla.markenx.api.classroom.students.application.ports.outgoing.CourseDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter that implements the CourseDataPort from the students module.
 * This acts as an Anti-Corruption Layer, exposing course data
 * without coupling to internal course module types.
 */
@Component
@RequiredArgsConstructor
public class CourseDataAdapter implements CourseDataPort {

    private final CourseCommandRepository courseRepository;
    private final TermCommandRepository academicTermRepository;

    @Override
    public Optional<CourseInfo> findCourseById(String courseId) {
        try {
            var course = courseRepository.findById(courseId);
            if (course == null) {
                return Optional.empty();
            }

            var academicTerm = academicTermRepository.findById(course.getAcademicTermId());
            String term = academicTerm != null ? academicTerm.toString() : null;

            return Optional.of(new CourseInfo(
                    course.getId().value(),
                    course.getName(),
                    term,
                    null // teacherName - placeholder for future implementation
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
