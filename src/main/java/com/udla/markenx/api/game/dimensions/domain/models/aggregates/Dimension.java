package com.udla.markenx.api.game.dimensions.domain.models.aggregates;

import com.udla.markenx.api.game.dimensions.domain.exceptions.*;
import org.jspecify.annotations.NonNull;

public class Dimension {

    private final DimensionId id;
    private final String name;
    private final String description;
    private final double consumerExpectation;
    private final double productInitialOffer;

    // region Constructors

    public Dimension(
            DimensionId id,
            String name,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
        this.id = id;
        validateName(name);
        validateDescription(description);
        validateConsumerExpectation(consumerExpectation);
        validateProductInitialOffer(productInitialOffer);

        this.name = name;
        this.description = description;
        this.consumerExpectation = consumerExpectation;
        this.productInitialOffer = productInitialOffer;
    }

    public Dimension(
            String id,
            String name,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
        this.id = new DimensionId(id);
        validateName(name);
        validateDescription(description);
        validateConsumerExpectation(consumerExpectation);
        validateProductInitialOffer(productInitialOffer);

        this.name = name;
        this.description = description;
        this.consumerExpectation = consumerExpectation;
        this.productInitialOffer = productInitialOffer;
    }

    // endregion

    // region Factories

    public static @NonNull Dimension create(
            String name,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
        var id = DimensionId.generate();
        return new Dimension(
                id,
                name,
                description,
                consumerExpectation,
                productInitialOffer
        );
    }

    // endregion

    // region Getters

    public DimensionId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getConsumerExpectation() {
        return consumerExpectation;
    }

    public double getProductInitialOffer() {
        return productInitialOffer;
    }

    // endregion

    // region Validations

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDimensionNameException();
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidDimensionDescriptionException();
        }
    }

    private void validateConsumerExpectation(double consumerExpectation) {
        if (consumerExpectation < 0 || consumerExpectation > 1) {
            throw new InvalidConsumerExpectationException();
        }
    }

    private void validateProductInitialOffer(double productInitialOffer) {
        if (productInitialOffer < 0 || productInitialOffer > 1) {
            throw new InvalidProductInitialOfferException();
        }
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}