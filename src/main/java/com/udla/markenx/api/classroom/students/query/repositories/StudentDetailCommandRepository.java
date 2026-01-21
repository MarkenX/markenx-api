package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentDetailPortDTO;

public interface StudentDetailCommandRepository {
    void upsert(StudentDetailPortDTO model);
}