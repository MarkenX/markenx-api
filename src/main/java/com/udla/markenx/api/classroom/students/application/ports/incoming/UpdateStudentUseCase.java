package com.udla.markenx.api.classroom.students.application.ports.incoming;

import com.udla.markenx.api.classroom.students.application.commands.DisableStudentCommand;
import com.udla.markenx.api.classroom.students.application.commands.UpdateStudentCommand;
import com.udla.markenx.api.classroom.students.application.queries.GetStudentByIdQuery;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

public interface UpdateStudentUseCase {
    Student getById(GetStudentByIdQuery query);
    void markIdentityCreated(String studentId);
    void markIdentityCreationFailed(String studentId);
    void onUserIdentityCreated(String studentId, String userId);
    void disable(DisableStudentCommand command);
    Student update(UpdateStudentCommand command);
    void onUserDisabled(String studentId);
    void onUserDisableFailed(String studentId);
}
