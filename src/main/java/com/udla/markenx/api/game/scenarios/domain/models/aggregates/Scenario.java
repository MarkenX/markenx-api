package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

public class Scenario {

    private final ScenarioId id;
    private long code;
    private String title;
    private String description;

    public Scenario(
            ScenarioId id,
            String title,
            String description
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
