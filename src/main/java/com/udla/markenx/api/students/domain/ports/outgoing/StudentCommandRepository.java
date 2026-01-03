package com.udla.markenx.api.students.domain.ports.outgoing;

import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface StudentCommandRepository {
    Student save(Student student);
    Student findById(String id);
    void update(Student student);
}
