CREATE TABLE student_user_read (
    student_id BINARY(16) PRIMARY KEY,
    user_id    BINARY(16) NOT NULL,
    email      VARCHAR(255),

    INDEX idx_student_user_read_user_id (user_id)
);
