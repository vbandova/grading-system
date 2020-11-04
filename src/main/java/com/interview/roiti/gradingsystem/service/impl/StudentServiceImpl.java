package com.interview.roiti.gradingsystem.service.impl;

import com.interview.roiti.gradingsystem.exception.GradingSystemException;
import com.interview.roiti.gradingsystem.model.Student;
import com.interview.roiti.gradingsystem.repository.MarkRepository;
import com.interview.roiti.gradingsystem.repository.StudentRepository;
import com.interview.roiti.gradingsystem.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository repository;
    private final MarkRepository markRepository;

    public StudentServiceImpl(StudentRepository studentRepository, MarkRepository markRepository) {
        this.repository = studentRepository;
        this.markRepository = markRepository;
    }

    @Override
    public Student get(Long id) {
        checkNotNull(id, "Cannot find student with null id.");
        logger.info("Retrieving student with id={}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkNotNull(id, "Cannot delete student with null id.");
        logger.info("Deleting student with id={}", id);
        if (!repository.existsById(id)) {
            throw new GradingSystemException("Cannot delete student with id=" + id + ". It does not exist!");
        }
        markRepository.deleteAllByStudentId(id);
        repository.deleteById(id);

    }

    @Override
    public Student create(String fullName) {
        checkArgument(StringUtils.isNotBlank(fullName), "Cannot create student with no name.");
        logger.info("Creating student with name={}", fullName);
        Student s = new Student();
        s.setFullName(fullName.trim());
        return repository.save(s);
    }

    @Override
    public Student update(Long id, String newFullName) {
        checkNotNull(id, "Cannot delete student with null id.");
        checkArgument(StringUtils.isNotBlank(newFullName), "Cannot update student name to no name.");
        logger.info("Updating student id={} with name={}", id, newFullName);
        Student student = get(id);
        if (student == null) {
            throw new GradingSystemException("Student with id=" + id + " does not exist. Cannot update");
        }
        student.setFullName(newFullName);
        return repository.save(student);
    }

    @Override
    public List<Student> getAll() {
        logger.info("Retrieving all students");
        return repository.findAll();
    }
}
