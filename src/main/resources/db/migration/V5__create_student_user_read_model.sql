CREATE TABLE student_summary_read_model (
    student_id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    INDEX idx_student_email (email)
);
