package com.udla.markenx.api.classroom.courses.application.handlers;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.mappers.CoursePortMapper;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.courses.domain.exceptions.TermNotUpcomingException;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ValidateTermUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCourseHandler implements CreateCourseUseCase {

    private final ValidateTermUseCase validateTermUseCase;
    private final CourseCommandRepository repository;
    private final CoursePortMapper mapper = new CoursePortMapper();

    private void ensureTermIsUpcoming(@NonNull CreateCourseCommand command) {
        var query = new IsUpcomingTermQuery(command.termId());
        if (!validateTermUseCase.isUpcoming(query)) {
            throw new TermNotUpcomingException(command.termId());
        }
    }

    @Override
    public CoursePortDTO handle(@NonNull CreateCourseCommand command) {
        if (!command.isHistorical()) {
            ensureTermIsUpcoming(command);
        }
        var course = Course.create(command.name(), command.termId());
        return mapper.toDTO(repository.save(course));
    }
}
