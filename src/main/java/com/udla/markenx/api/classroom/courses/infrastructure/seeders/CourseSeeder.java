package com.udla.markenx.api.classroom.courses.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.QueryTermsUseCase;
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
@Order(2)
@RequiredArgsConstructor
public class CourseSeeder extends BaseSeeder implements CommandLineRunner {

    private static final List<String> COURSE_NAMES = List.of(
            "Marketing Digital I",
            "Marketing Digital II"
    );

    private final CreateCourseUseCase createCourseUseCase;
    private final QueryTermsUseCase queryTermsUseCase;

    @Override
    public String name() {
        return "Courses";
    }

    @Override
    protected void doSeed() {
        upcomingTerms().forEach(this::seedCoursesForTerm);
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private @NonNull List<TermPortDTO> upcomingTerms() {
        return queryTermsUseCase.listTerms().stream()
                .filter(TermPortDTO::isUpcoming)
                .toList();
    }

    private void seedCoursesForTerm(TermPortDTO term) {
        COURSE_NAMES.forEach(courseName ->
                createCourse(courseName, term.id())
        );
    }

    private void createCourse(String courseName, String termId) {
        var command = new CreateCourseCommand(courseName, termId, true);
        createCourseUseCase.handle(command);
    }
}
