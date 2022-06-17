package domain.exceptions;

import domain.Course;

public class AlreadyPassedCourseException extends EnrollmentRulesViolationException {
    public AlreadyPassedCourseException(Course course) {
        super(String.format("The student has already passed %s", course.getName()));
    }
}
