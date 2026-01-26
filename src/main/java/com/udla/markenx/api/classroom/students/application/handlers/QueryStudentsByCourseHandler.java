package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentsByCourseQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsByCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentQueryRepository;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryStudentsByCourseHandler implements QueryStudentsByCourseUseCase {

    private final StudentQueryRepository studentQueryRepository;

    @Override
    public List<String> getStudentIdsByCourse(@NonNull StudentsByCourseQuery query) {
        return studentQueryRepository.findByCourseId(query.courseId())
                .stream()
                .map(Student::getId)
                .toList();
    }
}
