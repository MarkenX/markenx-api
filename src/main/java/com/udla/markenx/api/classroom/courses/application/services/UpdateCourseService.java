package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.out.TermValidationPort;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.UpdateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCourseService implements UpdateCourseUseCase {

    private final TermValidationPort termValidationPort;
    private final CourseCommandRepository repository;

    @Override
    public Course getById(@NonNull CourseIdQueryCriteria query) {
        return repository.findById(query.id());
    }

    @Override
    public Course changeAcademicTerm(@NonNull ChangeTermCommand command) {
        termValidationPort.ensureExists(command.academicTermId());
        Course course = repository.findById(command.id());
        course.changeAcademicTerm(command.academicTermId());
        return repository.save(course);
    }

    @Override
    public Course changeStatus(@NonNull ChangeStatusCommand command) {
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
