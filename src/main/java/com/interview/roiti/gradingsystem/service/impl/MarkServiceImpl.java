package com.interview.roiti.gradingsystem.service.impl;

import com.interview.roiti.gradingsystem.exception.GradingSystemException;
import com.interview.roiti.gradingsystem.model.Mark;
import com.interview.roiti.gradingsystem.repository.MarkRepository;
import com.interview.roiti.gradingsystem.rest.request.AverageMarkCombinationRequest;
import com.interview.roiti.gradingsystem.service.CourseService;
import com.interview.roiti.gradingsystem.service.MarkService;
import com.interview.roiti.gradingsystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
class MarkServiceImpl implements MarkService {
    private static final Logger logger = LoggerFactory.getLogger(MarkServiceImpl.class);

    private final MarkRepository repository;
    private final StudentService studentService;
    private final CourseService courseService;
    //tova e kato dnevnik ? 1 student za 1 predmet --> mnogo ocenki


    public MarkServiceImpl(MarkRepository repository, StudentService studentService, CourseService courseService) {
        this.repository = repository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Override
    public Mark get(Long id) {
        checkNotNull(id, "Cannot find mark with null id.");
        logger.info("Retrieving mark with id={}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Async
    public void delete(Long id) {
        checkNotNull(id, "Cannot delete mark with null id.");
        logger.info("Deleting mark with id={}", id);
        try {
            Thread.sleep(15_000);
        } catch (InterruptedException e) {
            System.out.println("Error while sleeping");
            e.printStackTrace();
        }
        if (!repository.existsById(id)) {
            throw new GradingSystemException("Cannot delete mark with id=" + id + ". It does not exist!");
        }
        System.out.println("FINISHED WITH MARK SERVICE");
        repository.deleteById(id);
    }

    @Override
    public Mark create(double value, long studentId, long courseId) {
        logger.info("Creating mark with value={}", value);
        if (!isValidMark(value)) {
            throw new GradingSystemException("Marks should be between 2 and 6, invalid value=" + value);
        }
        validateArguments(studentId, courseId);

        Mark m = new Mark();
        m.setValue(value);
        m.setStudentId(studentId);
        m.setCourseId(courseId);
        m.setTimestamp(LocalDate.now());
        return repository.save(m);
    }

    @Override
    public Mark update(Long id, double newValue) {
        checkNotNull(id, "Cannot delete mark with null id.");
        logger.info("Updating mark id={} with newMark={}", id, newValue);
        if (!isValidMark(newValue)) {
            throw new GradingSystemException("Marks should be between 2 and 6, invalid value=" + newValue);
        }

        Mark mark = get(id);
        if (mark == null) {
            throw new GradingSystemException("Mark with id=" + id + " does not exist. Cannot update");
        }
        mark.setValue(newValue);
        return repository.save(mark);
    }

    @Override
    public List<Mark> getAll() {
        logger.info("Retrieving all marks");
        return repository.findAll();
    }

    @Override
    public Double getAverageMarkForStudentInCourse(long studentId, long courseId) {
        validateArguments(studentId, courseId);
        return getAverageMark(repository.findAllByStudentIdAndCourseId(studentId, courseId));
    }

    @Override
    public Double getAverageMarkForStudentInAllCourses(long studentId) {
        validateStudent(studentId);
        return getAverageMark(repository.findAllByStudentId(studentId));
    }

    @Override
    public Double getAverageMarkForAllStudentsInCourse(long courseId) {
        validateCourse(courseId);
        return getAverageMark(repository.findAllByCourseId(courseId));
    }

    @Override
    public Double getAverageMarkForAllStudentsInAllCourses() {
        return getAverageMark(repository.findAll());
    }

    @Override
    public Double getAverageMarkForStudentsInCourses(AverageMarkCombinationRequest request) {
        //empty ids == all possible
        validateRequest(request);
        List<Long> studentIds = request.getStudentIds();
        List<Long> courseIds = request.getCourseIds();
        if (CollectionUtils.isEmpty(courseIds) && CollectionUtils.isEmpty(studentIds)) {
            return getAverageMarkForAllStudentsInAllCourses();
        }
        if (CollectionUtils.isEmpty(courseIds)) {
            return getAverageMark(repository.findAllByStudentIdIn(studentIds));
        }
        if (CollectionUtils.isEmpty(studentIds)) {
            return getAverageMark(repository.findAllByCourseIdIn(courseIds));
        }
        return getAverageMark(repository.findAllByStudentIdInAndCourseIdIn(studentIds, courseIds));
    }

    private void validateRequest(AverageMarkCombinationRequest request) {
        checkNotNull(request, "Request for average mark calculation cannot be null");

        if (!CollectionUtils.isEmpty(request.getCourseIds())) {
            request.getCourseIds().forEach(this::validateCourse);
        }
        if (!CollectionUtils.isEmpty(request.getStudentIds())) {
            request.getStudentIds().forEach(this::validateStudent);
        }

    }

    private boolean isValidMark(double mark) {
        return mark >= 2 && mark <= 6;
    }

    private void validateArguments(long studentId, long courseId) {
        validateCourse(courseId);
        validateStudent(studentId);

    }

    private void validateStudent(long studentId) {
        if (studentService.get(studentId) == null) {
            throw new GradingSystemException("Student with id=" + studentId + " does not exit");
        }
    }

    private void validateCourse(long courseId) {
        if (courseService.get(courseId) == null) {
            throw new GradingSystemException("Course with id=" + courseId + " does not exit");
        }
    }

    private Double getAverageMark(List<Mark> marks) {
        return marks.stream()
                .mapToDouble(Mark::getValue)
                .average()
                .orElse(-1.0);
    }
}