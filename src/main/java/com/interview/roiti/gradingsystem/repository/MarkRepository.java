package com.interview.roiti.gradingsystem.repository;

import com.interview.roiti.gradingsystem.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    void deleteAllByCourseId(Long id);

    void deleteAllByStudentId(Long id);

    List<Mark> findAllByCourseId(Long id);

    List<Mark> findAllByCourseIdIn(List<Long> id);

    List<Mark> findAllByStudentId(Long id);

    List<Mark> findAllByStudentIdIn(List<Long> id);

    List<Mark> findAllByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Mark> findAllByStudentIdInAndCourseIdIn(List<Long> studentIds, List<Long> courseIds);

}
