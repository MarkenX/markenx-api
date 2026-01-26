-- Add missing columns to student_summary_read_model for unified StudentResponseDTO
ALTER TABLE student_summary_read_model
    ADD COLUMN code INT NOT NULL DEFAULT 0,
    ADD COLUMN course_id CHAR(36) NOT NULL DEFAULT '',
    ADD COLUMN lifecycle_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- Populate existing rows with data from students table
UPDATE student_summary_read_model rm
    INNER JOIN students s ON rm.student_id = s.id
SET rm.code = s.code,
    rm.course_id = s.course_id,
    rm.lifecycle_status = s.lifecycle_status;

-- Add index for course_id queries
CREATE INDEX idx_student_summary_course ON student_summary_read_model (course_id);
