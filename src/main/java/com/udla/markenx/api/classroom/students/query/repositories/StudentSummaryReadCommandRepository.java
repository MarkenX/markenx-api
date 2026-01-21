package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentDetailPortDTO;

public interface StudentSummaryReadCommandRepository {
    void upsert(StudentDetailPortDTO model);
}