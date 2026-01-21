package com.udla.markenx.api.classroom.courses.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.courses.domain.exceptions.CourseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(2)
@RequiredArgsConstructor
public class CourseSeeder implements CommandLineRunner {

    private final CreateCourseUseCase createCourseUseCase;
    private final QueryTermsUseCase queryTermsUseCase;

    private static final List<String> COURSE_NAMES = List.of(
            "Marketing Digital",
            "Marketing Estratégico"
    );

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding courses...");

        List<TermPortDTO> academicTermsIds = queryTermsUseCase.listTerms();

        try {
            academicTermsIds.forEach(term -> {
                if (!term.isUpcoming()) return;
                COURSE_NAMES.forEach(courseName -> {
                    var command = new CreateCourseCommand(courseName, term.id(), true);
                    CoursePortDTO saved = createCourseUseCase.handle(command);
                    log.info("Course created: {} (id: {})", saved.label(), saved.id());
                });
            });
            log.info("Courses seeded successfully.");
        } catch (CourseException e) {
            log.error(e.getMessage(), e);
            log.info("Courses seeding failed.");
        }
    }
}