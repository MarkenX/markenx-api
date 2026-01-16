package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.classroom.students.application.ports.outgoing.CourseDataPort;
import com.udla.markenx.api.classroom.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryPagedReadRepository;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentQueryService implements StudentQueryUseCase {

    private final StudentSummaryPagedReadRepository pagedRepository;
    private final StudentSummaryReadRepository readRepository;
    private final StudentCommandRepository studentRepository;
    private final CourseDataPort courseDataPort;

    @Override
    public Page<StudentSummaryReadModel> getAllPaginated(@NonNull GetAllStudentsPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return pagedRepository.findAllPaginated(pageable);
    }

    @Override
    public Optional<StudentSummaryReadModel> findByEmail(String email) {
        return readRepository.findByEmail(email);
    }

    @Override
    public Optional<CourseDataPort.CourseInfo> findCourseByStudentId(String studentId) {
        try {
            var student = studentRepository.findById(studentId);
            if (student == null) {
                return Optional.empty();
            }
            return courseDataPort.findCourseById(student.getCourseId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
