CREATE TABLE users (
    id CHAR(36) NOT NULL,
    lifecycle_status VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,

    CONSTRAINT pk_courses
      PRIMARY KEY (id)
);
