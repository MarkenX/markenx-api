package com.udla.markenx.api.students.infrastructure.persistance.jdbc;

import com.udla.markenx.api.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.students.query.repositories.StudentSummaryReadRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStudentSummaryReadRepository
        implements StudentSummaryReadRepository {

    private final JdbcTemplate jdbc;

    public JdbcStudentSummaryReadRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void upsert(@NonNull StudentSummaryReadModel model) {
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

    @Override
    public Optional<StudentSummaryReadModel> findByStudentId(String studentId) {
        return jdbc.query("""
            SELECT student_id, email, full_name
            FROM student_summary_read_model
            WHERE student_id = ?
        """,
                rs -> rs.next()
                        ? Optional.of(new StudentSummaryReadModel(
                        rs.getString("student_id"),
                        rs.getString("email"),
                        rs.getString("full_name")
                ))
                        : Optional.empty(),
                studentId
        );
    }

    @Override
    public List<StudentSummaryReadModel> findAll() {
        return jdbc.query("""
            SELECT student_id, email, full_name
            FROM student_summary_read_model
        """,
                (rs, i) -> new StudentSummaryReadModel(
                        rs.getString("student_id"),
                        rs.getString("email"),
                        rs.getString("full_name")
                ));
    }
}
