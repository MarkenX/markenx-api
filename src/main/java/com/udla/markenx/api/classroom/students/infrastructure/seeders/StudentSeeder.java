package com.udla.markenx.api.classroom.students.infrastructure.seeders;

import com.udla.markenx.api.classroom.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.incoming.FindAllCoursesIdsForStudentsHandler;
import com.udla.markenx.api.classroom.students.application.ports.incoming.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.domain.exceptions.StudentException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
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

    private final FindAllCoursesIdsForStudentsHandler findAllCoursesIdsForStudents;
    private final RegisterStudentUseCase registerStudentUseCase;
    private final Flyway flyway;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding students...");

        List<String> coursesIds = findAllCoursesIdsForStudents.handle();

        try {
            coursesIds.forEach(courseId -> {
               var query = new RegisterStudentCommand(
                       "Mateo David", "Guamán Mora", courseId, "dmora@udla.edu.ec", true);
                Student saved = registerStudentUseCase.handle(query);
                log.info("The student {} was created", saved.toString());
            });
            log.info("Students seeded successfully.");
        } catch (StudentException e) {
            log.error(e.getMessage(), e);
            log.info("Students seeding failed.");
        }
    }
}
