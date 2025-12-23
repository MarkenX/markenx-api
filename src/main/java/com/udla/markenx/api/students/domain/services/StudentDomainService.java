package com.udla.markenx.api.students.domain.services;

import com.udla.markenx.api.students.domain.exceptions.StudentEmailAlreadyExistsException;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

public class StudentDomainService {

    /**
     * Ensures that the email address of the given student is unique within the provided collection of students.
     * If a student with the same email address already exists in the collection, an exception is thrown.
     *
     * @param students the collection of existing students whose email addresses must be checked for uniqueness
     * @param student the student whose email address is being validated for uniqueness
     * @throws StudentEmailAlreadyExistsException if the email address of the given student is already in use
     */
    public static void ensureEmailIsUnique(@NonNull Collection<Student> students, Student student) {
        students.forEach(s -> {
            if (s.getEmail().equals(student.getEmail())) {
                throw new StudentEmailAlreadyExistsException(student.getEmail().toString());
            }
        });
    }
}
