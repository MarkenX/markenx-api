package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.out.CourseDataPort;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface StudentQueryUseCase {
    Page<StudentSummaryReadModel> getAllPaginated(GetAllStudentsPaginatedQuery query);
    Optional<StudentSummaryReadModel> findByEmail(String email);
    Optional<CourseDataPort.CourseInfo> findCourseByStudentId(String studentId);
}
