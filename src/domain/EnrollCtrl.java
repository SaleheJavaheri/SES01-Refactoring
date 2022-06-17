package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
    public void enroll(Student student, List<CourseSection> courseSections) throws EnrollmentRulesViolationException {
        validateCourseSections(student, courseSections);
        student.takeCourseSection(courseSections);
    }

    private void validateCourseSections(Student student, List<CourseSection> targetCourseSection) throws EnrollmentRulesViolationException {
        for (CourseSection courseSection : targetCourseSection) {
            checkForAlreadyPassedCourse(courseSection, student);
            checkForPrerequisites(courseSection, student);
            checkForExamTimeConflict(targetCourseSection, courseSection);
            checkForDuplicatedRequest(targetCourseSection, courseSection);
        }
        checkForGpaLimit(targetCourseSection, student);
    }

    private void checkForDuplicatedRequest(List<CourseSection> courses, CourseSection targetCourseSection) throws EnrollmentRulesViolationException {
        for (CourseSection courseSection : courses) {
            if (targetCourseSection == courseSection)
                continue;
            if (targetCourseSection.getCourse().equals(courseSection.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", targetCourseSection.getCourse().getName()));
        }
    }

    private void checkForExamTimeConflict(List<CourseSection> courses, CourseSection targetCourseSection) throws EnrollmentRulesViolationException {
        for (CourseSection courseSection : courses) {
            if (targetCourseSection == courseSection)
                continue;
            if (targetCourseSection.getExamTime().equals(courseSection.getExamTime()))
                throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", targetCourseSection, courseSection));
        }
    }

    private void checkForPrerequisites(CourseSection courseSection, Student student) throws EnrollmentRulesViolationException {
        for (Course prerequisite : courseSection.getCourse().getPrerequisites()) {
            isPassedCourseSection(courseSection, student, prerequisite);
        }
    }

    private void isPassedCourseSection(CourseSection courseSection, Student student, Course course) throws EnrollmentRulesViolationException {
        if (student.getTerms().values().stream().noneMatch(studentTerm -> studentTerm.hasPassed(course)))
            throw new EnrollmentRulesViolationException(
                    String.format("The student has not passed %s as a prerequisite of %s",
                            course, courseSection.getCourse().getName())
            );
    }

    private void checkForAlreadyPassedCourse(CourseSection courseSection, Student student) throws EnrollmentRulesViolationException {
        if (student.getTerms().values().stream().anyMatch(studentTerm -> studentTerm.hasPassed(courseSection.getCourse())))
            throw new EnrollmentRulesViolationException(
                    String.format("The student has already passed %s", courseSection.getCourse().getName())
            );
    }

    private void checkForGpaLimit(List<CourseSection> courses, Student student) throws EnrollmentRulesViolationException {
        int unitsRequested = 0;
        for (CourseSection o : courses)
            unitsRequested += o.getCourse().getUnits();
        if ((student.getGpa() < 12 && unitsRequested > 14) ||
                (student.getGpa() < 16 && unitsRequested > 16) ||
                (unitsRequested > 20))
            throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, student.getGpa()));
    }

}
