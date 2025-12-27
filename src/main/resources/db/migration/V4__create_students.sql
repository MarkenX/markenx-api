CREATE TABLE students (
    id CHAR(36) NOT NULL,
    lifecycle_status VARCHAR(20) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    code INT NOT NULL AUTO_INCREMENT,
    course_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,

    CONSTRAINT pk_courses
        PRIMARY KEY (id),

    CONSTRAINT uk_courses_code
        UNIQUE (code),

    CONSTRAINT fk_students_course
        FOREIGN KEY (course_id)
            REFERENCES courses (id)
                ON DELETE RESTRICT
                ON UPDATE CASCADE,

    CONSTRAINT fk_students_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
                ON DELETE RESTRICT
                ON UPDATE CASCADE
);

CREATE INDEX idx_students_course
    ON students (course_id);

CREATE INDEX idx_students_user
    ON students (user_id);
