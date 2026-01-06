package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioDescriptionException;
import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioTitleException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Scenario {

    private final ScenarioId id;
    private long code;
    private String title;
    private String description;

    // region Constructors

    public Scenario(
            ScenarioId id,
            String title,
            String description
    ) {
        this.id = id;
        this.title = validateTitle(title);
        this.description = validateDescription(description);
    }

    public Scenario(
            String id,
            String title,
            String description
    ) {
        this.id = new ScenarioId(id);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
    }

    // endregion

    // region Factories

    public static @NotNull Scenario create(
            String title,
            String description
    ) {
        var id = ScenarioId.generate();
        return new Scenario(id, title, description);
    }

    // endregion

    // region Getters

    public String getId() {
        return id.value();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // endregion

    // region Setters

    public void setTitle(String title) {
        this.title = validateTitle(title);
    }

    public void setDescription(String description) {
        this.description = validateDescription(description);
    }

    // endregion

    // region Validations

    public String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidScenarioTitleException();
        }
        return title;
    }

    public String validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidScenarioDescriptionException();
        }
        return description;
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scenario that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion

    @Contract(pure = true)
    protected @NonNull String formatCode() {
        return String.format("%04d", code);
    }

    @Override
    public String toString() {
        return String.format("SCN-%s", formatCode());

    }
}
