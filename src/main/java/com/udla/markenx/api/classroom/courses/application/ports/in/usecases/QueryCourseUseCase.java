package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface QueryCourseUseCase {
    CoursePortDTO getCourseById(CourseIdQuery query);
    Page<CoursePortDTO> listCoursesPage(CoursePageQueryCriteria criteria);
}
