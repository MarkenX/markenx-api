package com.udla.markenx.api.students.application.services;


import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.application.ports.incoming.SaveStudentUseCase;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveStudentService implements SaveStudentUseCase {

    @Override
    public Student handle(SaveStudentCommand command) {
        return null;
    }
}
