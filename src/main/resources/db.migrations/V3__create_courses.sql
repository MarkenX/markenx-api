USE markenx;

CREATE TABLE courses (
     id CHAR(36) NOT NULL,
     lifecycle_status VARCHAR(20) NOT NULL,
     name VARCHAR(255) NOT NULL,
     code INT NOT NULL AUTO_INCREMENT,
     academic_term_id CHAR(36) NOT NULL,

     CONSTRAINT pk_courses
         PRIMARY KEY (id),

     CONSTRAINT uk_courses_code
         UNIQUE (code),

     CONSTRAINT fk_courses_academic_term
         FOREIGN KEY (academic_term_id)
             REFERENCES academic_terms (id)
             ON DELETE RESTRICT
             ON UPDATE CASCADE
);

CREATE INDEX idx_courses_academic_term
    ON courses (academic_term_id);
