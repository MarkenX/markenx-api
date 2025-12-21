package com.udla.markenx.api.courses.application.services;

import com.udla.markenx.api.academicterms.application.ports.incoming.UpdateAcademicTermUseCase;
import com.udla.markenx.api.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCourseService implements SaveCourseUseCase {

    private final UpdateAcademicTermUseCase updateAcademicTermUseCase;
    private final CourseRepository repository;

    @Override
    public Course handle(@NonNull SaveCourseCommand command) {

        GetAcademicTermByIdQuery query = new GetAcademicTermByIdQuery(command.academicTermId());
        AcademicTerm academicTerm = updateAcademicTermUseCase.getById(query);

        Course newCourse = Course.create(command.name(), academicTerm.getId().toString());
        return repository.save(newCourse);
    }
}
