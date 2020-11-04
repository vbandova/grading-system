package com.interview.roiti.gradingsystem.rest;

import com.interview.roiti.gradingsystem.model.Course;
import com.interview.roiti.gradingsystem.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/course", produces = "application/json")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.get(id);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestParam String name) {
        Course course = courseService.create(name);
        return ResponseEntity.ok(course);
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourseName(@PathVariable Long id, @RequestParam String updatedName) {
        Course updatedCourse = courseService.update(id, updatedName);
        return ResponseEntity.ok(updatedCourse);
    }
}
