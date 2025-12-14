package com.udla.markenx.api.shared.infrastructure.persistance.jpa;

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

    public JpaEntity(String id) {
        this.id = id;
    }
}
