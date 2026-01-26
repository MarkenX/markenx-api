package com.udla.markenx.api.classroom.students.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentDetailQueryRepository;
import com.udla.markenx.api.classroom.students.infrastructure.seeders.factories.StudentSeedFactory;
import com.udla.markenx.api.classroom.students.infrastructure.seeders.valueobjects.StudentSeedDefinition;
import com.udla.markenx.api.shared.infrastructure.seeders.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Seeder idempotente para estudiantes.
 * Solo inserta estudiantes que no existen (verificados por email como clave natural).
 * Es seguro ejecutar múltiples veces sin duplicar datos.
 */
@Slf4j
@Component
@Profile("dev")
@Order(3)
@RequiredArgsConstructor
public class StudentSeeder extends BaseSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final RegisterStudentUseCase registerStudentUseCase;
    private final StudentDetailQueryRepository studentQueryRepository;

    @Override
    public String name() {
        return "Students";
    }

    @Override
    protected void doSeed() {
        var courses = availableCourses();
        if (courses.isEmpty()) {
            log.warn("No courses found. Skipping student seeding.");
            return;
        }
        seedStudentsAcrossCourses(courses);
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private List<CoursePortDTO> availableCourses() {
        return queryCourseUseCase.listCourses();
    }

    /**
     * Obtiene los emails de los estudiantes ya registrados.
     */
    private Set<String> getExistingEmails() {
        return studentQueryRepository.findAll().stream()
                .map(StudentDetailPortDTO::email)
                .collect(Collectors.toSet());
    }

    private void seedStudentsAcrossCourses(List<CoursePortDTO> courses) {
        var students = StudentSeedFactory.defaultStudents();
        Set<String> existingEmails = getExistingEmails();

        for (int i = 0; i < students.size(); i++) {
            var student = students.get(i);

            if (studentExists(student.email(), existingEmails)) {
                continue;
            }

            var course = courses.get(i % courses.size());
            registerStudent(student, course.id());
        }
    }

    /**
     * Verifica si un estudiante ya existe usando el email como clave natural.
     */
    private boolean studentExists(String email, Set<String> existingEmails) {
        boolean exists = existingEmails.contains(email);
        if (exists) {
            log.debug("Student already exists, skipping: email={}", email);
        }
        return exists;
    }

    private void registerStudent(@NonNull StudentSeedDefinition student, String courseId) {
        var command = new RegisterStudentCommand(
                student.firstName(),
                student.lastName(),
                courseId,
                student.email(),
                true
        );

        registerStudentUseCase.handle(command);
        log.debug("Student registered: email={}", student.email());
    }
}
