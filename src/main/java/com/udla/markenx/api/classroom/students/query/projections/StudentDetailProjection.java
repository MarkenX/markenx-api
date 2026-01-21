package com.udla.markenx.api.classroom.students.query.projections;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.query.models.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.query.repositories.StudentDetailCommandRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentDetailProjection {

    private final StudentDetailCommandRepository repository;

    public StudentDetailProjection(StudentDetailCommandRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(StudentRegisteredEvent event) {
        repository.upsert(
                new StudentDetailPortDTO(
                        event.studentId(),
                        event.email(),
                        event.fullName()
                )
        );
    }
}
