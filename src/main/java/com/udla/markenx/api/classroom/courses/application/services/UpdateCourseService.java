package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.mappers.CoursePortMapper;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.UpdateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.exceptions.TermNotUpcomingException;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ValidateTermUseCase;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCourseService implements UpdateCourseUseCase {

    private final ValidateTermUseCase validateTermUseCase;
    private final CourseCommandRepository repository;
    private final CoursePortMapper mapper = new CoursePortMapper();

    private void ensureTermIsUpcoming(@NonNull ChangeTermCommand command) {
        var query = new IsUpcomingTermQuery(command.termId());
        if (!validateTermUseCase.isUpcoming(query)) {
            throw new TermNotUpcomingException(command.termId());
        }
    }

    @Override
    public CoursePortDTO getById(@NonNull CourseIdQueryCriteria query) {
        return mapper.toDTO(repository.findById(query.id()));
    }

    @Override
    public CoursePortDTO changeTerm(@NonNull ChangeTermCommand command) {
        ensureTermIsUpcoming(command);
        Course course = repository.findById(command.id());
        course.changeAcademicTerm(command.termId());
        return mapper.toDTO(repository.save(course));
    }

    @Override
    public CoursePortDTO changeStatus(@NonNull ChangeStatusCommand command) {
        Course course = repository.findById(command.id());
        if (command.targetStatus() == LifecycleStatus.ACTIVE) {
            course.enable();
        } else {
            course.disable();
        }
        return mapper.toDTO(repository.save(course));
    }

    @Override
    public CoursePortDTO update(@NonNull UpdateCourseCommand command) {
        Course course = repository.findById(command.id());
        course.update(command.name());
        return mapper.toDTO(repository.save(course));
    }
}
