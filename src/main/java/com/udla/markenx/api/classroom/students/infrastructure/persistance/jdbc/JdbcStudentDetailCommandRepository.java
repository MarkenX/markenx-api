package com.udla.markenx.api.classroom.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentDetailCommandRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcStudentDetailCommandRepository implements StudentDetailCommandRepository {

    private final JdbcTemplate jdbc;

    public JdbcStudentDetailCommandRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void upsert(@NonNull StudentDetailPortDTO model) {
        jdbc.update("""
            INSERT INTO student_summary_read_model
              (student_id, email, full_name, code, course_id, lifecycle_status)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
              email = VALUES(email),
              full_name = VALUES(full_name),
              code = VALUES(code),
              course_id = VALUES(course_id),
              lifecycle_status = VALUES(lifecycle_status)
        """,
                model.studentId(),
                model.email(),
                model.fullName(),
                model.code(),
                model.courseId(),
                model.lifecycleStatus());
    }

    @Override
    public void updateDetails(@NonNull String studentId, @NonNull String fullName, @NonNull String courseId) {
        jdbc.update("""
            UPDATE student_summary_read_model
            SET full_name = ?, course_id = ?
            WHERE student_id = ?
        """, fullName, courseId, studentId);
    }

    @Override
    public void updateStatus(@NonNull String studentId, @NonNull String lifecycleStatus) {
        jdbc.update("""
            UPDATE student_summary_read_model
            SET lifecycle_status = ?
            WHERE student_id = ?
        """, lifecycleStatus, studentId);
    }
}
