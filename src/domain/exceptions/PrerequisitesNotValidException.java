package domain.exceptions;

import domain.Course;

public class PrerequisitesNotValidException extends EnrollmentRulesViolationException{
    public PrerequisitesNotValidException(Course prerequisite, Course requestedCourse) {
        super(String.format("The student has not passed %s as a prerequisite of %s",
                prerequisite, requestedCourse.getName()));
    }
}
