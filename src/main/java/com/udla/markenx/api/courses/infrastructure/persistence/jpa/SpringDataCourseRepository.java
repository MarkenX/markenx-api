package com.udla.markenx.api.courses.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCourseRepository extends JpaRepository<CourseJpaEntity, String> {

}
