package com.udla.markenx.api.shared.infrastructure.persistance.jpa;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "entities")
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaEntity {
    @Id
    private String id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LifecycleStatus lifecycleStatus;

    public JpaEntity(String id, LifecycleStatus stlifecycleStatus) {
        this.id = id;
        this.lifecycleStatus = stlifecycleStatus;
    }
}
