package com.college.student;

import java.util.ArrayList;
import java.util.List;

/** Q1 - Student class: private fields, constructors, getters/setters, methods. */
public class Student {

    private int studentId;
    private String studentName;
    private String email;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;
    private List<String> technicalSkills = new ArrayList<>();

    // ----- Constructors -----
    public Student() {                       // needed to read JSON
    }

    public Student(int studentId, String studentName, String email, String courseName,
                   double programmingScore, double mathematicsScore, List<String> technicalSkills) {
        setStudentId(studentId);
        setStudentName(studentName);
        setEmail(email);
        setCourseName(courseName);
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        setTechnicalSkills(technicalSkills);
    }

    // ----- Getters -----
    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getCourseName() { return courseName; }
    public double getProgrammingScore() { return programmingScore; }
    public double getMathematicsScore() { return mathematicsScore; }
    public List<String> getTechnicalSkills() { return technicalSkills; }

    // ----- Setters (with validation) -----
    public void setStudentId(int studentId) {
        if (studentId <= 0) throw new IllegalArgumentException("Student ID must be positive");
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        if (studentName == null || studentName.isBlank()) throw new IllegalArgumentException("Name is required");
        this.studentName = studentName.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = email.trim();
    }

    public void setCourseName(String courseName) {
        if (courseName == null || courseName.isBlank()) throw new IllegalArgumentException("Course is required");
        this.courseName = courseName.trim();
    }

    public void setProgrammingScore(double score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("Programming score must be 0-100");
        this.programmingScore = score;
    }

    public void setMathematicsScore(double score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("Mathematics score must be 0-100");
        this.mathematicsScore = score;
    }

    public void setTechnicalSkills(List<String> skills) {
        this.technicalSkills = (skills == null) ? new ArrayList<>() : new ArrayList<>(skills);
    }

    // ----- Object methods -----
    public double getAverageScore() {
        return (programmingScore + mathematicsScore) / 2;
    }

    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85) return "Excellent";
        if (avg >= 70) return "Good";
        if (avg >= 50) return "Satisfactory";
        return "Needs Improvement";
    }

    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore) return "Programming";
        if (mathematicsScore > programmingScore) return "Mathematics";
        return "Equal";
    }

    @Override
    public String toString() {
        return "Student{" + studentId + ", " + studentName + ", " + email + ", " + courseName
                + ", P=" + programmingScore + ", M=" + mathematicsScore + ", skills=" + technicalSkills + "}";
    }
}
