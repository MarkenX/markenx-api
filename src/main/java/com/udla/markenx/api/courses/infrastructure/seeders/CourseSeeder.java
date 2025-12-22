package com.udla.markenx.api.courses.infrastructure.seeders;

import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.ports.incoming.FindAllAcademicTermIds;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.courses.domain.exceptions.CourseException;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
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

    private final FindAllAcademicTermIds findAllAcademicTermIds;
    private final SaveCourseUseCase saveCourseUseCase;
    private final Flyway flyway;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding courses...");

        List<String> academicTermsIds = findAllAcademicTermIds.findAllIds();

        try {
            academicTermsIds.forEach(termId -> {
                var query = new SaveCourseCommand("Test", termId);
                Course saved = saveCourseUseCase.handle(query);
                log.info("The course {} was created", saved.toString());
            });
            log.info("Courses seeded successfully.");
        } catch (CourseException e) {
            log.error(e.getMessage(), e);
            log.info("Academic terms seeding failed.");
        }
    }
}