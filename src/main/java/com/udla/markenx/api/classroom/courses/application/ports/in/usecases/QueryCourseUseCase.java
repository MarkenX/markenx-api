package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CoursePageQueryCriteria;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseStatusQueryCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryCourseUseCase {
    CoursePortDTO getCourseById(CourseIdQuery query);
    List<CoursePortDTO> listCourses();
    Page<CoursePortDTO> listCoursesPage(CoursePageQueryCriteria criteria);
    List<CoursePortDTO> listCoursesByStatus(CourseStatusQueryCriteria criteria);
}
