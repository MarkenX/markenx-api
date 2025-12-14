package com.udla.markenx.api.shared.infrastructure.persistance.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "entities")
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaEntity {
    @Id
    private String id;
}
