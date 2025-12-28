package com.udla.markenx.api.students.application.services;


import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.students.application.ports.incoming.EnsureCourseExists;
import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.users.application.ports.incoming.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveStudentService implements SaveStudentUseCase {

    private final EnsureCourseExists ensureCourseExists;
    private final StudentCommandRepository repository;
    private final CreateUserUseCase createUserUseCase;

    @Override
    public Student handle(@NonNull SaveStudentCommand command) {
        ensureCourseExists.ensureExists(command.courseId());

        var createUserCommand = new CreateUserCommand(command.email(), "STUDENT");
        String userId = createUserUseCase.handle(createUserCommand);

        Student newStudent = Student.create(
                command.firstName(),
                command.lastName(),
                command.courseId(),
                userId
        );
        return repository.save(newStudent);
    }
}
