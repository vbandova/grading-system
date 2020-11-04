package com.interview.roiti.gradingsystem.service;

import com.interview.roiti.gradingsystem.model.Course;

import java.util.List;

public interface CourseService {

    Course get(Long id);

    void delete(Long id);

    Course create(String name);

    Course update(Long id, String newName);

    List<Course> getAll();
}
