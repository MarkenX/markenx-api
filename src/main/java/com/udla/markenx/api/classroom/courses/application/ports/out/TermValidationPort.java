package com.udla.markenx.api.classroom.courses.application.ports.out;

public interface TermValidationPort {
    void ensureExists(String termId);
    void ensureIsUpcoming(String termId);
}
