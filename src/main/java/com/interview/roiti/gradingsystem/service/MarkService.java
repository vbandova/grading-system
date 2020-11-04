package com.interview.roiti.gradingsystem.service;

import com.interview.roiti.gradingsystem.model.Mark;
import com.interview.roiti.gradingsystem.rest.request.AverageMarkCombinationRequest;

import java.util.List;

public interface MarkService {

    Mark get(Long id);

    void delete(Long id);

    Mark create(double value, long studentId, long courseId);

    Mark update(Long id, double newValue);

    List<Mark> getAll();

    Double getAverageMarkForStudentInCourse(long studentId, long courseId);

    Double getAverageMarkForStudentInAllCourses(long studentId);

    Double getAverageMarkForAllStudentsInCourse(long course);

    Double getAverageMarkForAllStudentsInAllCourses();

    Double getAverageMarkForStudentsInCourses(AverageMarkCombinationRequest request);

}
