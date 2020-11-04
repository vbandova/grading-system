package com.interview.roiti.gradingsystem.rest;

import com.interview.roiti.gradingsystem.model.User;
import com.interview.roiti.gradingsystem.rest.request.AverageMarkCombinationRequest;
import com.interview.roiti.gradingsystem.service.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/mark/average", produces = "application/json")
public class AverageController {
    private final MarkService markService;

    public AverageController(MarkService markService) {
        this.markService = markService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getAllMarksAverage() {
        Double avg = markService.getAverageMarkForAllStudentsInAllCourses();
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Double> getStudentAverageForAllCourses(@PathVariable long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUser().equals("student") && !user.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Double avg = markService.getAverageMarkForStudentInAllCourses(id);
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/course/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getCourseAverageForAllStudents(@PathVariable long id) {
        Double avg = markService.getAverageMarkForAllStudentsInCourse(id);
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Double> getStudentAverageForCourse(@PathVariable long studentId, @PathVariable long courseId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUser().equals("student") && !user.getId().equals(studentId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Double avg = markService.getAverageMarkForStudentInCourse(studentId, courseId);
        return ResponseEntity.ok(avg);
    }

    @PostMapping
    public ResponseEntity<Double> calculateRandomCombination(@RequestBody AverageMarkCombinationRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUser().equals("student") && request != null && request.getStudentIds() != null &&
                (request.getStudentIds().size() != 1 || request.getStudentIds().size() == 1 && !request.getStudentIds().contains(user.getId()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Double avg = markService.getAverageMarkForStudentsInCourses(request);
        return ResponseEntity.ok(avg);
    }


}
