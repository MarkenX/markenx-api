package com.udla.markenx.api.classroom.courses.infrastructure.seeders;

import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ListTermsUseCase;
import com.udla.markenx.api.classroom.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.SaveCourseUseCase;
import com.udla.markenx.api.classroom.courses.domain.exceptions.CourseException;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
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

    private final SaveCourseUseCase saveCourseUseCase;
    private final ListTermsUseCase listTermsUseCase;

    private static final List<String> COURSE_NAMES = List.of(
            "Marketing Digital",
            "Marketing Estratégico"
    );

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding courses...");

        List<TermPortDTO> academicTermsIds = listTermsUseCase.listTerms();

        try {
            academicTermsIds.forEach(term -> {
                if (!term.isUpcoming()) return;
                COURSE_NAMES.forEach(courseName -> {
                    var command = new SaveCourseCommand(courseName, term.id(), true);
                    Course saved = saveCourseUseCase.handle(command);
                    log.info("Course created: {} (id: {})", saved.getName(), saved.getId());
                });
            });
            log.info("Courses seeded successfully.");
        } catch (CourseException e) {
            log.error(e.getMessage(), e);
            log.info("Courses seeding failed.");
        }
    }
}