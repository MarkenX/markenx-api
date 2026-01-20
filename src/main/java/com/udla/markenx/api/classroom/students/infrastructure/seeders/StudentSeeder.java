package com.udla.markenx.api.classroom.students.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.domain.exceptions.StudentException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
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
@Order(3)
@RequiredArgsConstructor
public class StudentSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final RegisterStudentUseCase registerStudentUseCase;

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

        List<CoursePortDTO> courses = queryCourseUseCase.listCourses();
        if (courses.isEmpty()) {
            log.warn("No courses found, skipping student seeding.");
            return;
        }

        try {
            for (StudentData student : STUDENTS) {
                // Distribute students across courses
                CoursePortDTO course = courses.getFirst();
                var command = new RegisterStudentCommand(
                        student.firstName(),
                        student.lastName(),
                        course.id(),
                        student.email(),
                        true
                );
                Student saved = registerStudentUseCase.handle(command);
                log.info("Student created: {} {} (id: {})",
                        student.firstName(), student.lastName(), saved.getId());
            }
            log.info("Students seeded successfully. Total: {}", STUDENTS.size());
        } catch (StudentException e) {
            log.error(e.getMessage(), e);
            log.info("Students seeding failed.");
        }
    }
}
