package com.udla.markenx.api.courses.infrastructure.seeders;

import com.udla.markenx.api.academicterms.application.ports.incoming.AcademicTermQueryUseCase;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.application.ports.incoming.SaveCourseUseCase;
import com.udla.markenx.api.courses.domain.exceptions.CourseException;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;
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

    private final AcademicTermQueryUseCase academicTermQueryUseCase;
    private final SaveCourseUseCase service;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding courses...");

        List<AcademicTerm> academicTerms = academicTermQueryUseCase.getAll();

        try {
            academicTerms.forEach(term -> {
                var query = new SaveCourseCommand("Test", term.getId().toString());
                Course saved = service.handle(query);
                log.info("The course {} was created", saved.toString());
            });
            log.info("Courses seeded successfully.");
        } catch (CourseException e) {
            log.error(e.getMessage(), e);
            log.info("Academic terms seeding failed.");
        }
    }
}