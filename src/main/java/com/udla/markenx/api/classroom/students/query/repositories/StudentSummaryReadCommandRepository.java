package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;

public interface StudentSummaryReadCommandRepository {
    void upsert(StudentSummaryReadModel model);
}