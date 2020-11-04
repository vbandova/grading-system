package com.interview.roiti.gradingsystem.rest.request;

import java.util.List;

public class AverageMarkCombinationRequest {
    private List<Long> studentIds;
    private List<Long> courseIds;

    public AverageMarkCombinationRequest() {
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
