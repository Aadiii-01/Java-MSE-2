package com.college.student;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/** Business rules and the use of Java collections. */
@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Student add(Student s) {
        if (s.getStudentId() <= 0 || s.getStudentName() == null || s.getEmail() == null || s.getCourseName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "studentId, studentName, email and courseName are required");
        }
        if (repo.existsById(s.getStudentId())) {                                  // no duplicate IDs
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student ID already exists");
        }
        if (repo.existsByEmail(s.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        repo.insert(s);
        return s;
    }

    public List<Student> getAll() {
        return repo.findAll();
    }

    public Student getById(int id) {
        Student s = repo.findById(id);
        if (s == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student " + id + " not found");
        }
        return s;
    }

    public List<Student> search(Integer id, String email, String course) {
        if (id != null) {
            return List.of(getById(id));
        }
        if (email != null) {
            return repo.findByEmail(email);
        }
        if (course != null) {
            return repo.findByCourse(course);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Give id, email or course");
    }

//    @Transactional
//    public Student update(int id, Student body) {
//        Student s = getById(id);
//        s.setProgrammingScore(body.getProgrammingScore());
//        s.setMathematicsScore(body.getMathematicsScore());
//        s.setTechnicalSkills(body.getTechnicalSkills());
//        repo.updateScoresAndSkills(s);
//        return s;
//    }
    @Transactional
    public Student update(int id, Student body) {
        Student s = getById(id);

        if (body.getStudentName() != null) {
            s.setStudentName(body.getStudentName());
        }
        if (body.getEmail() != null && !body.getEmail().equalsIgnoreCase(s.getEmail())) {
            if (repo.existsByEmail(body.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
            }
            s.setEmail(body.getEmail());
        }
        if (body.getCourseName() != null) {
            s.setCourseName(body.getCourseName());
        }

        s.setProgrammingScore(body.getProgrammingScore());
        s.setMathematicsScore(body.getMathematicsScore());
        s.setTechnicalSkills(body.getTechnicalSkills());

        repo.updateScoresAndSkills(s);
        return s;
    }

    @Transactional
    public void delete(int id) {
        if (!repo.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student " + id + " not found");
        }
    }

    /** Merit list: highest average first. */
    public List<Student> meritList() {
        List<Student> list = new ArrayList<>(repo.findAll());
        list.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return list;
    }

    /** Students whose skill sets are identical. Key = sorted skills, value = student names. */
    public Map<String, List<String>> sameSkills() {
        Map<String, List<String>> groups = new TreeMap<>();
        for (Student s : repo.findAll()) {
            if (s.getTechnicalSkills().isEmpty()) {
                continue;
            }
            TreeSet<String> skills = new TreeSet<>();
            for (String skill : s.getTechnicalSkills()) {
                skills.add(skill.toLowerCase());
            }
            groups.computeIfAbsent(String.join(", ", skills), k -> new ArrayList<>()).add(s.getStudentName());
        }
        groups.values().removeIf(names -> names.size() < 2);                      // keep only real matches
        return groups;
    }

    public List<Student> needsProgrammingHelp() {
        return repo.findWeakProgrammingStrongMaths();
    }
}
