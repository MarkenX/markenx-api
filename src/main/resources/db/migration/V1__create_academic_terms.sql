USE markenx;

CREATE TABLE academic_terms (
    id CHAR(36) NOT NULL,
    lifecycle_status VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    academic_year INT NOT NULL,
    sequence INT NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT pk_academic_terms
        PRIMARY KEY (id),

    CONSTRAINT uk_academic_terms_year_sequence
        UNIQUE (academic_year, sequence)
);
