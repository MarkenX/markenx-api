package com.udla.markenx.api.game.consumers.domain.models.aggregates;

import com.udla.markenx.api.game.consumers.domain.exceptions.*;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Consumer {

    private final ConsumerId id;
    private final String name;
    private final Integer age;
    private final BigDecimal budget;
    private final double targetAcceptanceScore;

    // region Constructors

    public Consumer(
            ConsumerId id,
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
        this.id = id;
        validateName(name);
        validateAge(age);
        validateBudget(budget);
        validateTargetAcceptanceScore(targetAcceptanceScore);

        this.name = name;
        this.age = age;
        this.budget = budget;
        this.targetAcceptanceScore = targetAcceptanceScore;
    }

    public Consumer(
            String id,
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
        this.id = new ConsumerId(id);
        validateName(name);
        validateAge(age);
        validateBudget(budget);
        validateTargetAcceptanceScore(targetAcceptanceScore);

        this.name = name;
        this.age = age;
        this.budget = budget;
        this.targetAcceptanceScore = targetAcceptanceScore;
    }

    // endregion

    // region Factories

    public static @NonNull Consumer create(
            String name,
            Integer age,
            BigDecimal budget,
            double targetAcceptanceScore
    ) {
        var id = ConsumerId.generate();
        return new Consumer(
                id,
                name,
                age,
                budget,
                targetAcceptanceScore
        );
    }

    // endregion

    // region Getters

    public ConsumerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public double getTargetAcceptanceScore() {
        return targetAcceptanceScore;
    }

    // endregion

    // region Validations

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidConsumerNameException();
        }
    }

    private void validateAge(Integer age) {
        if (age != null && (age < 0 || age > 150)) {
            throw new InvalidConsumerAgeException();
        }
    }

    private void validateBudget(BigDecimal budget) {
        if (budget != null && budget.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidConsumerBudgetException();
        }
    }

    private void validateTargetAcceptanceScore(double targetAcceptanceScore) {
        if (targetAcceptanceScore < 0 || targetAcceptanceScore > 1) {
            throw new InvalidTargetAcceptanceScoreException();
        }
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consumer that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}
