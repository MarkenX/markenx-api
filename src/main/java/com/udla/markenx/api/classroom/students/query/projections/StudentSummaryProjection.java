package com.udla.markenx.api.classroom.students.query.projections;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadCommandRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentSummaryProjection {

    private final StudentSummaryReadCommandRepository repository;

    public StudentSummaryProjection(StudentSummaryReadCommandRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(StudentRegisteredEvent event) {
        repository.upsert(
                new StudentSummaryReadModel(
                        event.studentId(),
                        event.email(),
                        event.fullName()
                )
        );
    }
}
