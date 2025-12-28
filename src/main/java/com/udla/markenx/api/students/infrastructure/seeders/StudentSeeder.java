package com.udla.markenx.api.students.infrastructure.seeders;

import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.ports.incoming.FindAllCoursesIds;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import com.udla.markenx.api.students.domain.exceptions.StudentException;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
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
@Order(3)
@RequiredArgsConstructor
public class StudentSeeder implements CommandLineRunner {

    private final FindAllCoursesIds findAllCoursesIds;
    private final SaveStudentUseCase saveStudentUseCase;
    private final Flyway flyway;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding students...");

        List<String> coursesIds = findAllCoursesIds.findAllIds();

        try {
            coursesIds.forEach(courseId -> {
               var query = new SaveStudentCommand(
                       "John", "Doe", courseId, "test@udla.edu.ec");
                Student saved = saveStudentUseCase.handle(query);
                log.info("The student {} was created", saved.toString());
            });
            log.info("Students seeded successfully.");
        } catch (StudentException e) {
            log.error(e.getMessage(), e);
            log.info("Students seeding failed.");
        }
    }
}
