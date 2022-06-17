package domain;

import java.util.HashMap;
import java.util.Map;

public class StudentTerm {
    public static final int COURSE_PASSED_GRADE_LIMIT = 10;
    private Map<Course, Double> grades = new HashMap<>();

    public void addGrade(Course course, Double grade) {
        grades.put(course, grade);
    }

    public Double getGrade(Course course) {
        return grades.get(course);
    }

    public Map<Course, Double> getGrades() {
        return grades;
    }

    public boolean hasPassed(Course course) {
        Double grade = grades.get(course);
        return grade != null && grade >= COURSE_PASSED_GRADE_LIMIT;
    }
}
