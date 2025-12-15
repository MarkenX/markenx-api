package com.udla.markenx.api.academicterms.infrastructure.tasks;

import com.udla.markenx.api.academicterms.application.services.AcademicTermQueryService;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AcademicTermStatusScheduler {
    private final AcademicTermQueryService service;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateAcademicPeriodStatuses() {
        List<AcademicTerm> terms = service.getAllExcludingStatus(AcademicTermStatus.ENDED);
        terms.forEach(AcademicTerm::refreshStatus);
    }
}
