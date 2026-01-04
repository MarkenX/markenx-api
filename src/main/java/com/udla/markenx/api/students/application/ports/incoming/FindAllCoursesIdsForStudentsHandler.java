package com.udla.markenx.api.students.application.ports.incoming;

import java.util.List;

public interface FindAllCoursesIdsForStudentsHandler {
    List<String> handle();
}
