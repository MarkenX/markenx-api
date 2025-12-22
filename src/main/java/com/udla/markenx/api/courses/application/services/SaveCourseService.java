package com.udla.markenx.api.courses.application.services;

import com.udla.markenx.api.academicterms.application.ports.incoming.UpdateAcademicTermUseCase;
import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.ports.incoming.EnsureAcademicTermExists;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCourseService implements SaveCourseUseCase {

    private final EnsureAcademicTermExists ensureAcademicTermExists;
    private final CourseCommandRepository repository;

    @Override
    public Course handle(@NonNull SaveCourseCommand command) {
        ensureAcademicTermExists.ensureExists(command.academicTermId());

        Course newCourse = Course.create(command.name(), command.academicTermId());
        return repository.save(newCourse);
    }
}
