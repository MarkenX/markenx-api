package com.udla.markenx.api.classroom.students.application.ports.out;

public interface StudentDetailCommandRepository {
    void upsert(StudentDetailPortDTO model);
}