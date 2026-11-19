package com.college.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping                                   // 1. add a student
    @ResponseStatus(HttpStatus.CREATED)
    public Student add(@RequestBody Student student) {
        return service.add(student);
    }

    @GetMapping                                    // 2. all students
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")                           // 3. search by ID
    public Student getById(@PathVariable("id") int id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")                           // 4. update scores and skills
    public Student update(@PathVariable("id") int id, @RequestBody Student body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")                        // 5. delete
    public Map<String, String> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Map.of("message", "Student " + id + " deleted");
    }

    @GetMapping("/merit-list")                     // 6. merit list
    public List<Student> meritList() {
        return service.meritList();
    }

    // ----- extra endpoints for Q2 -----
    @GetMapping("/search")                         // by id, email or course
    public List<Student> search(@RequestParam(name = "id", required = false) Integer id,
                                @RequestParam(name = "email", required = false) String email,
                                @RequestParam(name = "course", required = false) String course) {
        return service.search(id, email, course);
    }

    @GetMapping("/same-skills")
    public Map<String, List<String>> sameSkills() {
        return service.sameSkills();
    }

    @GetMapping("/programming-support")
    public List<Student> programmingSupport() {
        return service.needsProgrammingHelp();
    }
}
