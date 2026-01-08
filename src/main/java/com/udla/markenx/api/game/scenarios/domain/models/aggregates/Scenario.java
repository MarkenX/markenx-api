package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioDescriptionException;
import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioTitleException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Scenario {

    private final ScenarioId id;
    private long code;
    private String title;
    private String description;
    private String consumerId;
    private final List<String> dimensionIds;
    private final List<String> actionIds;
    private final List<String> eventIds;

    // region Constructors

    public Scenario(
            ScenarioId id,
            String title,
            String description
    ) {
        this.id = id;
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.consumerId = null;
        this.dimensionIds = new ArrayList<>();
        this.actionIds = new ArrayList<>();
        this.eventIds = new ArrayList<>();
    }

    public Scenario(
            String id,
            String title,
            String description
    ) {
        this.id = new ScenarioId(id);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.consumerId = null;
        this.dimensionIds = new ArrayList<>();
        this.actionIds = new ArrayList<>();
        this.eventIds = new ArrayList<>();
    }

    public Scenario(
            String id,
            String title,
            String description,
            String consumerId,
            List<String> dimensionIds,
            List<String> actionIds,
            List<String> eventIds
    ) {
        this.id = new ScenarioId(id);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.consumerId = consumerId;
        this.dimensionIds = dimensionIds != null ? new ArrayList<>(dimensionIds) : new ArrayList<>();
        this.actionIds = actionIds != null ? new ArrayList<>(actionIds) : new ArrayList<>();
        this.eventIds = eventIds != null ? new ArrayList<>(eventIds) : new ArrayList<>();
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

    public String getConsumerId() {
        return consumerId;
    }

    public List<String> getDimensionIds() {
        return Collections.unmodifiableList(dimensionIds);
    }

    public List<String> getActionIds() {
        return Collections.unmodifiableList(actionIds);
    }

    public List<String> getEventIds() {
        return Collections.unmodifiableList(eventIds);
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

    // region Association Methods

    public void associateConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public void addDimension(String dimensionId) {
        if (dimensionId != null && !dimensionIds.contains(dimensionId)) {
            dimensionIds.add(dimensionId);
        }
    }

    public void addAction(String actionId) {
        if (actionId != null && !actionIds.contains(actionId)) {
            actionIds.add(actionId);
        }
    }

    public void addEvent(String eventId) {
        if (eventId != null && !eventIds.contains(eventId)) {
            eventIds.add(eventId);
        }
    }

    public void removeDimension(String dimensionId) {
        dimensionIds.remove(dimensionId);
    }

    public void removeAction(String actionId) {
        actionIds.remove(actionId);
    }

    public void removeEvent(String eventId) {
        eventIds.remove(eventId);
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
