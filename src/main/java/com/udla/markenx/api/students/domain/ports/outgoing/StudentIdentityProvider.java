package com.udla.markenx.api.students.domain.ports.outgoing;

import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface StudentIdentityProvider {
    void createUser(Student student);
}
