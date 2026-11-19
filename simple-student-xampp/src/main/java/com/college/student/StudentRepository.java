package com.college.student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** All SQL lives here (JDBC through JdbcTemplate). */
@Repository
public class StudentRepository {

    private static final String SELECT = "SELECT * FROM students";

    private final JdbcTemplate jdbc;

    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Student> findAll() {
        return withSkills(jdbc.query(SELECT + " ORDER BY student_id", this::mapRow));
    }

    public Student findById(int id) {
        List<Student> list = withSkills(jdbc.query(SELECT + " WHERE student_id = ?", this::mapRow, id));
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Student> findByEmail(String email) {
        return withSkills(jdbc.query(SELECT + " WHERE LOWER(email) = LOWER(?)", this::mapRow, email));
    }

    public List<Student> findByCourse(String course) {
        return withSkills(jdbc.query(SELECT + " WHERE LOWER(course_name) LIKE LOWER(?)",
                this::mapRow, "%" + course + "%"));
    }

    /** Programming below 50 but mathematics 75 or more. */
    public List<Student> findWeakProgrammingStrongMaths() {
        return withSkills(jdbc.query(SELECT + " WHERE programming_score < 50 AND mathematics_score >= 75",
                this::mapRow));
    }

    public boolean existsById(int id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM students WHERE student_id = ?", Integer.class, id);
        return n != null && n > 0;
    }

    public boolean existsByEmail(String email) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM students WHERE LOWER(email) = LOWER(?)",
                Integer.class, email);
        return n != null && n > 0;
    }

    public void insert(Student s) {
        jdbc.update("INSERT INTO students (student_id, student_name, email, course_name, "
                        + "programming_score, mathematics_score) VALUES (?, ?, ?, ?, ?, ?)",
                s.getStudentId(), s.getStudentName(), s.getEmail(), s.getCourseName(),
                s.getProgrammingScore(), s.getMathematicsScore());
        saveSkills(s);
    }

//    public void updateScoresAndSkills(Student s) {
//        jdbc.update("UPDATE students SET programming_score = ?, mathematics_score = ? WHERE student_id = ?",
//                s.getProgrammingScore(), s.getMathematicsScore(), s.getStudentId());
//        jdbc.update("DELETE FROM student_skills WHERE student_id = ?", s.getStudentId());
//        saveSkills(s);
//    }
    public void updateScoresAndSkills(Student s) {
        jdbc.update("UPDATE students SET student_name = ?, email = ?, course_name = ?, "
                        + "programming_score = ?, mathematics_score = ? WHERE student_id = ?",
                s.getStudentName(), s.getEmail(), s.getCourseName(),
                s.getProgrammingScore(), s.getMathematicsScore(), s.getStudentId());
        jdbc.update("DELETE FROM student_skills WHERE student_id = ?", s.getStudentId());
        saveSkills(s);
    }

    public boolean deleteById(int id) {
        return jdbc.update("DELETE FROM students WHERE student_id = ?", id) > 0;   // skills cascade
    }

    // ----- helpers -----
    private Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("student_name"), rs.getString("email"),
                rs.getString("course_name"), rs.getDouble("programming_score"),
                rs.getDouble("mathematics_score"), new ArrayList<>());
    }

    private List<Student> withSkills(List<Student> students) {
        for (Student s : students) {
            s.setTechnicalSkills(jdbc.queryForList(
                    "SELECT skill_name FROM student_skills WHERE student_id = ?", String.class, s.getStudentId()));
        }
        return students;
    }

    private void saveSkills(Student s) {
        for (String skill : s.getTechnicalSkills()) {
            if (skill != null && !skill.isBlank()) {
                jdbc.update("INSERT IGNORE INTO student_skills (student_id, skill_name) VALUES (?, ?)",
                        s.getStudentId(), skill.trim());
            }
        }
    }
}
