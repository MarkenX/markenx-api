package com.udla.markenx.api.classroom.students.infrastructure.web.rest;

import com.udla.markenx.api.classroom.students.application.exceptions.StudentNotFoundException;
import com.udla.markenx.api.classroom.students.domain.exceptions.StudentException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.classroom.students")
public class StudentControllerAdvice {

    private static final String STUDENT_ERROR_CODE = "STUDENT_ERROR";
    private static final String STUDENT_NOT_FOUND_CODE = "STUDENT_NOT_FOUND";

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleStudentNotFoundException(StudentNotFoundException ex) {
        return new ErrorResponse(STUDENT_NOT_FOUND_CODE, ex.getMessage());
    }

    @ExceptionHandler(StudentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDomainException(StudentException ex) {
        return new ErrorResponse(STUDENT_ERROR_CODE, ex.getMessage());
    }
}
