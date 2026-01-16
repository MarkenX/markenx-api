package com.udla.markenx.api.classroom.courses.infrastructure.web;

import com.udla.markenx.api.classroom.courses.application.exceptions.CourseNotFoundException;
import com.udla.markenx.api.classroom.courses.domain.exceptions.CourseException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.classroom.courses")
public class CourseControllerAdvice {

    private static final String COURSE_ERROR_CODE = "COURSE_ERROR";
    private static final String COURSE_NOT_FOUND_CODE = "COURSE_NOT_FOUND";

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleCourseNotFoundException(@NonNull CourseNotFoundException ex) {
        return new ErrorResponse(COURSE_NOT_FOUND_CODE, ex.getMessage());
    }

    @ExceptionHandler(CourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDomainException(@NonNull CourseException ex) {
        return new ErrorResponse(COURSE_ERROR_CODE, ex.getMessage());
    }
}
