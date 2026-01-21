package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.DisableStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.UpdateStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentIdQuery;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

public interface UpdateStudentUseCase {
    Student getById(StudentIdQuery query);
    void markIdentityCreated(String studentId);
    void markIdentityCreationFailed(String studentId);
    void onUserIdentityCreated(String studentId, String userId);
    void disable(DisableStudentCommand command);
    Student update(UpdateStudentCommand command);
    void onUserDisabled(String studentId);
}
