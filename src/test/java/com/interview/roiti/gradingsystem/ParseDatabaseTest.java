package com.interview.roiti.gradingsystem;

import com.interview.roiti.gradingsystem.model.Course;
import com.interview.roiti.gradingsystem.model.Mark;
import com.interview.roiti.gradingsystem.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseDatabaseTest {
    private Map<Long, Student> students = new HashMap();
    private Map<Long, Course> courses = new HashMap();
    private Map<Long, Mark> marks = new HashMap<>();

    @Test
    void name() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("marks.csv").getFile()))) {

            String line = br.readLine();
            if (line != null) {
                System.out.println("Skipping " + line);
                line = br.readLine();//skip the first
            }
            while (line != null) {
                List<String> lineValues = Arrays.asList(line.split(","));
                if (lineValues.size() != 7) {
                    throw new RuntimeException("Issues with row: " + line);
                }
                Student student = new Student();
                student.setId(Long.valueOf(lineValues.get(0)));
                student.setFullName(cleanString(lineValues.get(1)));
                Course course = new Course();
                course.setId(Long.valueOf(lineValues.get(2)));
                course.setName(cleanString(lineValues.get(3)));
                Mark mark = new Mark();
                mark.setStudentId(student.getId());
                mark.setCourseId(course.getId());
                mark.setId(Long.valueOf(lineValues.get(4)));
                mark.setValue(Double.valueOf(lineValues.get(5)));
                String dateTime = lineValues.get(6).split(" ")[0];
                mark.setTimestamp(LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE));

                String fullName = student.getFullName();

                if (students.containsKey(student.getId()) && !students.get(student.getId()).equals(student)) {
                    throw new RuntimeException("Error with consistency of student: " + student);
                }
                if (courses.containsKey(course.getId()) && !courses.get(course.getId()).equals(course)) {
                    throw new RuntimeException("Error with consistency of course: " + course);
                }

                if (marks.containsKey(mark.getId()) && !marks.get(mark.getId()).equals(mark)) {
                    throw new RuntimeException("Error with consistency of mark: " + course);
                }
                students.putIfAbsent(student.getId(), student);
                courses.putIfAbsent(course.getId(), course);
                marks.putIfAbsent(mark.getId(), mark);

                line = br.readLine();
            }


            System.out.println("We have finalists");
        }
        students.forEach((k, v) ->
                System.out.println("insert into STUDENT(ID, FULL_NAME) values(" + k + ", '" + v.getFullName() + "');")
        );
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        courses.forEach((k, v) ->
                System.out.println("insert into COURSE(ID, NAME) values(" + k + ", '" + v.getName() + "');")
        );

        System.out.println("-----------------------------------");
        marks.forEach((k, v) ->
                System.out.println("insert into MARK(ID, MARK, STUDENT_ID, COURSE_ID, MARK_DATE) values("
                        + k + ", " + v.getValue() + ", " + v.getStudentId() + ", " +
                        v.getCourseId() + ", parsedatetime('" + v.getTimestamp().toString() + "', " +"'yyyy-MM-dd'"+ "));")
        );
    }

    private String cleanString(String value) {
        return value
                .replace("\"", "")
                .replace("'", "''")
                .trim();
    }
}
