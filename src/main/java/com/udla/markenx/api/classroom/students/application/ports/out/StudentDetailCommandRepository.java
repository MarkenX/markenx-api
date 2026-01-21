package com.udla.markenx.api.classroom.students.application.ports.out;

import com.udla.markenx.api.classroom.students.application.dtos.StudentDetailPortDTO;

public interface StudentDetailCommandRepository {
    void upsert(StudentDetailPortDTO model);
}