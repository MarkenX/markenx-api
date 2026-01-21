package com.udla.markenx.api.classroom.students.application.projections;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentDetailCommandRepository;
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
