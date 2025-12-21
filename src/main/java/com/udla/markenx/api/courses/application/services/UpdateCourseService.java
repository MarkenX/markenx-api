package com.udla.markenx.api.courses.application.services;

import com.udla.markenx.api.courses.application.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.courses.application.ports.incoming.UpdateCourseUseCase;
import com.udla.markenx.api.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.courses.domain.ports.outgoing.CourseCommandRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCourseService implements UpdateCourseUseCase {

    private final CourseCommandRepository repository;

    @Override
    public Course getById(@NonNull GetCourseByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    public Course changeStatus(@NonNull ChangeCourseStatusCommand command) {
        Course term = repository.findById(command.id());
        if (command.targetStatus() == LifecycleStatus.ACTIVE) {
            term.enable();
        } else {
            term.disable();
        }
        return repository.save(term);
    }

}
