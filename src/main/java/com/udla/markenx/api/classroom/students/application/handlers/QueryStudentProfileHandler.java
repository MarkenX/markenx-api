package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentProfilePortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentProfileQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentProfileUseCase;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryStudentProfileHandler implements QueryStudentProfileUseCase {

    private final StudentSummaryReadCommandRepository repository;
    private final QueryCourseUseCase queryCourseUseCase;
    private final QueryTermsUseCase queryTermsUseCase;


    @Override
    public StudentProfilePortDTO getById(StudentProfileQuery query) {
        Student student = repository.findByStudentId(query.studentId());
        CoursePortDTO course = queryCourseUseCase.getCourseById(new CourseIdQuery(student.getCourseId()));
        TermPortDTO term = queryTermsUseCase.getTermById(new TermIdQuery(course.termId()));

        return new StudentProfilePortDTO(
                student.getId(),
                student.get
        );
    }
}
