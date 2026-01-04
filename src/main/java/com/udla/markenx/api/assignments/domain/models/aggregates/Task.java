package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

public class Task extends Assignment {

    private Task(
            AssignmentId id,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentScore minScoreToPass,
            AssignmentStatus status,
            String academicTermId) {
        super(id, info, deadline, minScoreToPass, status, academicTermId);
    }
    public Task(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentScore minScoreToPass,
            AssignmentStatus status,
            String academicTermId
    ) {
        super(id, lifecycleStatus, code, info, deadline, minScoreToPass, status, academicTermId);
    }

    public static @NonNull Task create(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String academicTermId
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.future(deadline),
                minScoreToPass,
                AssignmentStatus.NOT_STARTED,
                academicTermId
        );
    }

    public static @NonNull Task createHistorical(
            AssignmentInfo info,
            LocalDateTime deadline,
            AssignmentScore minScoreToPass,
            String academicTermId
    ) {
        var id = AssignmentId.generate();
        return new Task(
                id,
                info,
                AssignmentDeadline.historical(deadline),
                minScoreToPass,
                AssignmentStatus.NOT_STARTED,
                academicTermId
        );
    }

    @Override
    public void updateStatus() {

    }
}
