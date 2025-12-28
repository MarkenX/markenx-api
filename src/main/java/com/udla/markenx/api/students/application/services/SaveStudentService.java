package com.udla.markenx.api.students.application.services;


import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.students.application.ports.incoming.EnsureCourseExists;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveStudentService implements SaveStudentUseCase {

    private final EnsureCourseExists ensureCourseExists;
    private final StudentCommandRepository repository;

    @Override
    public Student handle(@NonNull SaveStudentCommand command) {
        ensureCourseExists.ensureExists(command.courseId());

        Student newStudent = Student.create(
                command.firstName(),
                command.lastName(),
                command.courseId(),
                ""
        );
        return repository.save(newStudent);
    }
}
