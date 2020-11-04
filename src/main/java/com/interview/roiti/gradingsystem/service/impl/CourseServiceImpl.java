package com.interview.roiti.gradingsystem.service.impl;


import com.interview.roiti.gradingsystem.exception.GradingSystemException;
import com.interview.roiti.gradingsystem.model.Course;
import com.interview.roiti.gradingsystem.repository.CourseRepository;
import com.interview.roiti.gradingsystem.repository.MarkRepository;
import com.interview.roiti.gradingsystem.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository repository;
    private final MarkRepository markRepository;

    public CourseServiceImpl(CourseRepository repository, MarkRepository markRepository) {
        this.repository = repository;
        this.markRepository = markRepository;
    }

    @Override
    public Course get(Long id) {
        checkNotNull(id, "Cannot find course with null id.");
        logger.info("Retrieving course with id={}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkNotNull(id, "Cannot delete course with null id.");
        logger.info("Deleting course with id={}", id);
        if (!repository.existsById(id)) {
            throw new GradingSystemException("Cannot delete course with id=" + id + ". It does not exist!");
        }
        markRepository.deleteAllByCourseId(id);
        repository.deleteById(id);
    }

    @Override
    public Course create(String name) {
        checkArgument(StringUtils.isNotBlank(name), "Cannot create course with no name.");
        logger.info("Creating course with name={}", name);
        Course c = new Course();
        c.setName(name.trim());
        return repository.save(c);
    }

    @Override
    public Course update(Long id, String newName) {
        checkNotNull(id, "Cannot delete course with null id.");
        checkArgument(StringUtils.isNotBlank(newName), "Cannot update course name to no name.");
        logger.info("Updating course id={} with name={}", id, newName);
        Course course = get(id);
        if (course == null) {
            throw new GradingSystemException("Course with id=" + id + " does not exist. Cannot update");
        }
        course.setName(newName);
        return repository.save(course);
    }

    @Override
    public List<Course> getAll() {
        logger.info("Retrieving all courses");
        return repository.findAll();
    }
}
