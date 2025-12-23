package com.udla.markenx.api.students.domain.ports.outgoing;

import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface StudentCommandRepository {
    Student save(Student course);
    Student findById(String id);
}
