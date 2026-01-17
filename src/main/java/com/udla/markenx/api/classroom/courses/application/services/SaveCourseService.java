package com.udla.markenx.api.classroom.courses.application.services;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.SaveCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.EnsureTermIsUpcomingUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.SaveCourseUseCase;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCourseService implements SaveCourseUseCase {

    private final EnsureTermIsUpcomingUseCase ensureTermIsUpcoming;
    private final CourseCommandRepository repository;

    @Override
    public Course handle(@NonNull SaveCourseCommand command) {
        if (!command.isHistorical()) {
            ensureTermIsUpcoming.handle(command.academicTermId());
        }

        Course newCourse = Course.create(command.name(), command.academicTermId());
        return repository.save(newCourse);
    }
}
