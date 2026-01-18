package com.udla.markenx.api.classroom.courses.application.ports.in.commands;

public record CreateCourseCommand(String name, String academicTermId, boolean isHistorical) {
}
