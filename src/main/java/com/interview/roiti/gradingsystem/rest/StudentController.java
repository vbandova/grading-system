package com.interview.roiti.gradingsystem.rest;

import com.interview.roiti.gradingsystem.model.Student;
import com.interview.roiti.gradingsystem.model.User;
import com.interview.roiti.gradingsystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/student", produces = "application/json")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUser().equals("student") && !user.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Student student = studentService.get(id);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> createStudent(@RequestParam String fullName) {
        Student student = studentService.create(fullName);
        return ResponseEntity.ok(student);
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudentName(@PathVariable Long id, @RequestParam String updatedFullName) {
        Student updatedStudent = studentService.update(id, updatedFullName);
        return ResponseEntity.ok(updatedStudent);
    }
}
