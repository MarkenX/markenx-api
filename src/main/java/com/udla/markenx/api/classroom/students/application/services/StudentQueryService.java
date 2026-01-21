package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsUseCase;
import com.udla.markenx.api.classroom.students.application.ports.out.CourseDataPort;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadQueryRepository;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentQueryService implements QueryStudentsUseCase {

    private final StudentSummaryReadQueryRepository pagedRepository;
    private final StudentSummaryReadCommandRepository readRepository;
    private final StudentCommandRepository studentRepository;
    private final CourseDataPort courseDataPort;

    @Override
    public Page<StudentSummaryReadModel> getAllPaginated(@NonNull StudentPageQueryCriteria query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return pagedRepository.findAllPaginated(pageable);
    }

    @Override
    public StudentSummaryReadModel findByEmail(String email) {
        return pagedRepository.findByEmail(email);
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
