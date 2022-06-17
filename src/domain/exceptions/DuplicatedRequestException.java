package domain.exceptions;

import domain.Course;

public class DuplicatedRequestException extends EnrollmentRulesViolationException {
    public DuplicatedRequestException(Course course) {
        super(String.format("%s is requested to be taken twice", course.getName()));
    }
}
