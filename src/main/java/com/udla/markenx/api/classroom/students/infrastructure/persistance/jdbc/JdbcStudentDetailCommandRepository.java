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
              (student_id, email, full_name)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
              email = VALUES(email),
              full_name = VALUES(full_name)
        """,
                model.studentId(),
                model.email(),
                model.fullName());
    }
}
