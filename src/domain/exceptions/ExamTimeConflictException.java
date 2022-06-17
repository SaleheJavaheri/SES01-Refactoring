package domain.exceptions;

import domain.CourseSection;

public class ExamTimeConflictException extends EnrollmentRulesViolationException {
    public ExamTimeConflictException(CourseSection targetCourseSection, CourseSection courseSection) {
        super(String.format("Two offerings %s and %s have the same exam time", targetCourseSection, courseSection));
    }
}
