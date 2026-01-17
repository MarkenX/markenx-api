package com.udla.markenx.api.classroom.courses.application.ports.in.commands;

public record SaveCourseCommand(String name, String academicTermId, boolean isHistorical) {
}
