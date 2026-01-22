package com.udla.markenx.api.classroom.students.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
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

@Slf4j
@Component
@Profile("dev")
@Order(3)
@RequiredArgsConstructor
public class StudentSeeder extends BaseSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final RegisterStudentUseCase registerStudentUseCase;

    @Override
    public String name() {
        return "Students";
    }

    @Override
    protected void doSeed() {
        var courses = availableCourses();
        seedStudentsAcrossCourses(courses);
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private List<CoursePortDTO> availableCourses() {
        return queryCourseUseCase.listCourses();
    }

    private void seedStudentsAcrossCourses(List<CoursePortDTO> courses) {
        var students = StudentSeedFactory.defaultStudents();

        for (int i = 0; i < students.size(); i++) {
            var student = students.get(i);
            var course = courses.get(i % courses.size());
            registerStudent(student, course.id());
        }
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
    }
}
