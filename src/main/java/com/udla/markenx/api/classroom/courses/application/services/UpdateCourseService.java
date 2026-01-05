package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseAcademicTermCommand;
import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.classroom.courses.application.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.EnsureAcademicTermExists;
import com.udla.markenx.api.classroom.courses.application.ports.incoming.UpdateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.domain.ports.outgoing.CourseCommandRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCourseService implements UpdateCourseUseCase {

    private final EnsureAcademicTermExists ensureAcademicTermExists;
    private final CourseCommandRepository repository;

    @Override
    public Course getById(@NonNull GetCourseByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    public Course changeAcademicTerm(@NonNull ChangeCourseAcademicTermCommand command) {
        ensureAcademicTermExists.ensureExists(command.academicTermId());
        Course course = repository.findById(command.id());
        course.changeAcademicTerm(command.academicTermId());
        return repository.save(course);
    }

    @Override
    public Course changeStatus(@NonNull ChangeCourseStatusCommand command) {
        Course course = repository.findById(command.id());
        if (command.targetStatus() == LifecycleStatus.ACTIVE) {
            course.enable();
        } else {
            course.disable();
        }
        return repository.save(course);
    }

    @Override
    public Course update(@NonNull UpdateCourseCommand command) {
        Course course = repository.findById(command.id());
        course.update(command.name());
        return repository.save(course);
    }
}
