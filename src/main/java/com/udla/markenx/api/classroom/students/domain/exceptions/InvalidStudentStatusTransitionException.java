package com.udla.markenx.api.classroom.students.domain.exceptions;

import com.udla.markenx.api.classroom.students.domain.models.valueobjects.StudentStatus;

public final class InvalidStudentStatusTransitionException extends StudentException {

    private final StudentStatus current;
    private final StudentStatus target;

    public InvalidStudentStatusTransitionException(
            StudentStatus current,
            StudentStatus target
    ) {
        super(buildMessage(current, target));
        this.current = current;
        this.target = target;
    }

    private static String buildMessage(
            StudentStatus current,
            StudentStatus target
    ) {
        return "Invalid student status transition: "
                + current + " -> " + target;
    }

    public StudentStatus current() {
        return current;
    }

    public StudentStatus target() {
        return target;
    }
}
