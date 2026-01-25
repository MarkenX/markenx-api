package com.udla.markenx.api.classroom.courses.infrastructure.seeders;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.CreateCourseUseCase;
import com.udla.markenx.api.classroom.courses.application.ports.out.CourseQueryRepository;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Seeder idempotente para cursos.
 * Solo inserta cursos que no existen (verificados por nombre y término).
 * Es seguro ejecutar múltiples veces sin duplicar datos.
 */
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
    private final CourseQueryRepository courseQueryRepository;

    @Override
    public String name() {
        return "Courses";
    }

    @Override
    protected void doSeed() {
        Set<String> existingCourseKeys = getExistingCourseKeys();
        upcomingTerms().forEach(term -> seedCoursesForTerm(term, existingCourseKeys));
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

    /**
     * Obtiene las claves (nombre-termId) de los cursos existentes.
     */
    private Set<String> getExistingCourseKeys() {
        return courseQueryRepository.findAll().stream()
                .map(this::toCourseKey)
                .collect(Collectors.toSet());
    }

    private String toCourseKey(@NonNull Course course) {
        return course.getName() + "-" + course.getTermId();
    }

    private void seedCoursesForTerm(TermPortDTO term, Set<String> existingKeys) {
        COURSE_NAMES.stream()
                .filter(name -> !courseExists(name, term.id(), existingKeys))
                .forEach(courseName -> createCourse(courseName, term.id()));
    }

    /**
     * Verifica si un curso ya existe usando su clave natural (nombre-termId).
     */
    private boolean courseExists(String name, String termId, Set<String> existingKeys) {
        String key = name + "-" + termId;
        boolean exists = existingKeys.contains(key);
        if (exists) {
            log.debug("Course already exists, skipping: name={}, termId={}", name, termId);
        }
        return exists;
    }

    private void createCourse(String courseName, String termId) {
        var command = new CreateCourseCommand(courseName, termId, true);
        createCourseUseCase.handle(command);
        log.debug("Course created: name={}, termId={}", courseName, termId);
    }
}
