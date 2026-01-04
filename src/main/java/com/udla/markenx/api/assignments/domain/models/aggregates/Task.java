package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public class Task extends Assignment {

    public Task(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentStatus status,
            String academicTermId
    ) {
        super(id, lifecycleStatus, code, info, deadline, status, academicTermId);
    }

    @Override
    public void updateStatus() {

    }
}
