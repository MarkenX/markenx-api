package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.infrastructure.seeders.factories.TaskSeedFactory;
import com.udla.markenx.api.classroom.assignments.infrastructure.seeders.valueobjects.TaskSeedDefinition;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.shared.infrastructure.seeders.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(4)
@RequiredArgsConstructor
public class TaskSeeder extends BaseSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final CreateTaskUseCase createTaskUseCase;

    @Override
    public String name() {
        return "Tasks";
    }

    @Override
    protected void doSeed() {
        activeCourses().forEach(this::seedTasksForCourse);
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private List<CoursePortDTO> activeCourses() {
        return queryCourseUseCase.listCourses();
    }

    private void seedTasksForCourse(CoursePortDTO course) {
        TaskSeedFactory.forActiveCourse(referenceTime())
                .forEach(def -> createTask(def, course.id()));
    }

    private void createTask(@NonNull TaskSeedDefinition def, String courseId) {
        var command = new CreateTaskCommand(
                def.title(),
                def.description(),
                def.deadline(),
                def.acceptanceRate(),
                courseId,
                def.maxAttempts(),
                def.outdated()
        );

        createTaskUseCase.handle(command);
    }

    @Contract(" -> new")
    private @NonNull LocalDateTime referenceTime() {
        return LocalDateTime.now();
    }
}
