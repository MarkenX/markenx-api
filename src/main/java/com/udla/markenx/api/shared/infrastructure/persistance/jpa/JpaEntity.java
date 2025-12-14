package com.udla.markenx.api.shared.infrastructure.persistance.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "entities")
public class JpaEntity {
    @Id
    private String id;
}
