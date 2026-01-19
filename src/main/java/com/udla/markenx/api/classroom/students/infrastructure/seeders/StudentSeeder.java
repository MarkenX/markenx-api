package com.udla.markenx.api.classroom.students.infrastructure.seeders;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.FindAllCoursesIdsForStudentsHandler;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
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

    private record StudentData(String firstName, String lastName, String email) {}

    private static final List<StudentData> STUDENTS = List.of(
            new StudentData("Christian", "Jácome", "christian.jacome.mora@udla.edu.ec"),
            new StudentData("Ana", "Rodriguez", "ana.rodriguez@udla.edu.ec"),
            new StudentData("Luis", "Garcia", "luis.garcia@udla.edu.ec"),
            new StudentData("Sofia", "Martinez", "sofia.martinez@udla.edu.ec")
    );

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding students...");

        List<String> coursesIds = findAllCoursesIdsForStudents.handle();
        if (coursesIds.isEmpty()) {
            log.warn("No courses found, skipping student seeding.");
            return;
        }

        int studentIndex = 0;
        try {
            for (StudentData student : STUDENTS) {
                // Distribute students across courses
                String courseId = coursesIds.getFirst();
                var command = new RegisterStudentCommand(
                        student.firstName(),
                        student.lastName(),
                        courseId,
                        student.email(),
                        true
                );
                Student saved = registerStudentUseCase.handle(command);
                log.info("Student created: {} {} (id: {})",
                        student.firstName(), student.lastName(), saved.getId());
                studentIndex++;
            }
            log.info("Students seeded successfully. Total: {}", STUDENTS.size());
        } catch (StudentException e) {
            log.error(e.getMessage(), e);
            log.info("Students seeding failed.");
        }
    }
}
