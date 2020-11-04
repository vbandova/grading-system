package com.interview.roiti.gradingsystem.service;

import com.interview.roiti.gradingsystem.model.Student;

import java.util.List;

public interface StudentService {
    Student get(Long id);

    void delete(Long id);

    Student create(String fullName);

    Student update(Long id, String newFullName);

    List<Student> getAll();

}
