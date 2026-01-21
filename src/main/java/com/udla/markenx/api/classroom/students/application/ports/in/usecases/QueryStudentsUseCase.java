package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.out.CourseDataPort;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface QueryStudentsUseCase {
    Page<StudentSummaryReadModel> getAllPaginated(StudentPageQueryCriteria query);
    StudentSummaryReadModel findByEmail(String email);
    Optional<CourseDataPort.CourseInfo> findCourseByStudentId(String studentId);
}
