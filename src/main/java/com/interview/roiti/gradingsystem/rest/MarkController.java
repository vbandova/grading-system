package com.interview.roiti.gradingsystem.rest;

import com.interview.roiti.gradingsystem.model.Mark;
import com.interview.roiti.gradingsystem.model.User;
import com.interview.roiti.gradingsystem.service.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/mark", produces = "application/json")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Mark>> getAllMarks() {
        List<Mark> marks = markService.getAll();
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mark> getMark(@PathVariable Long id) {
        Mark mark = markService.get(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (mark!= null && user.getUser().equals("student") && !user.getId().equals(mark.getStudentId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(mark);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMark(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Mark> createMark(@RequestParam Double value, @RequestParam Long courseId, @RequestParam Long studentId) {
        Mark mark = markService.create(value, studentId, courseId);
        return ResponseEntity.ok(mark);
    }

    @PatchMapping("/{id}/value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Mark> updateMarkValue(@PathVariable Long id, @RequestParam Double newMark) {
        Mark updatedMark = markService.update(id, newMark);
        return ResponseEntity.ok(updatedMark);
    }
}
