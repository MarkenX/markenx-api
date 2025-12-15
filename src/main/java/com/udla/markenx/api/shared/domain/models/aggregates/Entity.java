package com.udla.markenx.api.shared.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyDisabledException;
import com.udla.markenx.api.shared.domain.exceptions.EntityAlreadyEnabledException;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.Getter;

@Getter
public abstract class Entity {

    protected LifecycleStatus lifecycleStatus;

    protected Entity() {
        this.lifecycleStatus = LifecycleStatus.ACTIVE;
    }

    protected Entity(LifecycleStatus lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public boolean isActive() {
        return lifecycleStatus == LifecycleStatus.ACTIVE;
    }

    public void disable() {
        if (this.lifecycleStatus == LifecycleStatus.DISABLED) {
            throw new EntityAlreadyDisabledException(this);
        }
        this.lifecycleStatus = LifecycleStatus.DISABLED;
    }

    public void enable() {
        if (this.lifecycleStatus == LifecycleStatus.ACTIVE) {
            throw new EntityAlreadyEnabledException(this);
        }
        this.lifecycleStatus = LifecycleStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "AggregateRoot{" +
                "lifecycleStatus=" + lifecycleStatus +
                '}';
    }
}
