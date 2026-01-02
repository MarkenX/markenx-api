package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.application.queries.GetStudentByIdQuery;
import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface UpdateStudentUseCase {
    Student getById(GetStudentByIdQuery query);
    void markIdentityCreated(String studentId);
    void markIdentityCreationFailed(String studentId);
    void onUserIdentityCreated(String studentId, String userId);
}
