package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.students.domain.models.valueobjects.Email;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Student extends Entity {

    private final StudentId id;
    private final PersonalInfo personalInfo;
    private final Long code;
    private final String courseId;

    public Student(StudentId id, Long code, String firstName, String lastName, Email email, String courseId) {
        this.id = id;
        this.code = code;
        this.personalInfo = new PersonalInfo(firstName, lastName, email);
        this.courseId = courseId;
    }

    public String getId() {
        return this.id.toString();
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Email getEmail() {
        return this.personalInfo.getEmail();
    }

    /**
     * Formats the course code as a zero-padded four-digit string.
     *
     * @return the formatted course code as a string
     */
    @Contract(pure = true)
    private @NonNull String formatCode() {
        return String.format("%04d", code);
    }

    @Override
    public String toString() {
        return String.format("STD-%s", formatCode());
    }
}
