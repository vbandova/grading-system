package com.interview.roiti.gradingsystem.model;

import javax.persistence.*;
import java.time.LocalDate;

//it will be best to hold all the data here
@Entity
@Table(name = "MARK")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mark_generator")
    @SequenceGenerator(name = "mark_generator", sequenceName = "mark_sequence", initialValue = 21627)
    private Long id;

    @Column(name = "MARK")
    private Double value;

    @Column(name = "MARK_DATE")
    private LocalDate timestamp;

    private Long studentId;

    private Long courseId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
