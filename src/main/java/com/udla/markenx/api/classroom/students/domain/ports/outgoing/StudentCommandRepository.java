package com.udla.markenx.api.classroom.students.domain.ports.outgoing;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

public interface StudentCommandRepository {
    Student save(Student student);
    void update(Student student);
}
