CREATE TABLE dimensions (
    id CHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    consumer_expectation DECIMAL(3,2) NOT NULL
        CHECK (consumer_expectation >= 0 AND consumer_expectation <= 1),
    product_initial_offer DECIMAL(3,2) NOT NULL
        CHECK (product_initial_offer >= 0 AND product_initial_offer <= 1),

    CONSTRAINT pk_dimensions
        PRIMARY KEY (id),

    CONSTRAINT uk_dimensions_name
        UNIQUE (name)
);

CREATE INDEX idx_dimensions_consumer_expectation
    ON dimensions (consumer_expectation);

CREATE INDEX idx_dimensions_product_initial_offer
    ON dimensions (product_initial_offer);