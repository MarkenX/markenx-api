package com.udla.markenx.api.classroom.academicterms.infrastructure.tasks;

import com.udla.markenx.api.classroom.academicterms.application.services.RefreshTermStatusesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TermStatusScheduler {
    private final RefreshTermStatusesService service;

    @Scheduled(cron = "0 0 0 * * *")
    public void refreshTermStatuses() {
        service.handle();
    }
}
