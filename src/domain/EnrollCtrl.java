package domain;

import java.util.List;

import domain.exceptions.*;

public class EnrollCtrl {

    public static final int ACADEMIC_PROBATION_GRADE_LIMIT = 12;
    public static final int ACADEMIC_PROBATION_UNITS_LIMIT = 14;
    public static final int DISTINGUISHED_GRADE_LIMIT = 16;
    public static final int NORMAL_STUDENT_UNITS_LIMIT = 16;
    public static final int DISTINGUISHED_STUDENT_UNITS_LIMITS = 20;

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
                throw new DuplicatedRequestException(courseSection.getCourse());
        }
    }

    private void checkForExamTimeConflict(List<CourseSection> courses, CourseSection targetCourseSection) throws EnrollmentRulesViolationException {
        for (CourseSection courseSection : courses) {
            if (targetCourseSection == courseSection)
                continue;
            if (targetCourseSection.getExamTime().equals(courseSection.getExamTime()))
                throw new ExamTimeConflictException(targetCourseSection, courseSection);
        }
    }

    private void checkForPrerequisites(CourseSection courseSection, Student student) throws EnrollmentRulesViolationException {
        for (Course prerequisite : courseSection.getCourse().getPrerequisites()) {
            isPassedCourseSection(courseSection, student, prerequisite);
        }
    }

    private void isPassedCourseSection(CourseSection courseSection, Student student, Course course) throws EnrollmentRulesViolationException {
        if (student.getTerms().values().stream().noneMatch(studentTerm -> studentTerm.hasPassed(course)))
            throw new PrerequisitesNotValidException(course, courseSection.getCourse());
    }

    private void checkForAlreadyPassedCourse(CourseSection courseSection, Student student) throws EnrollmentRulesViolationException {
        if (student.getTerms().values().stream().anyMatch(studentTerm -> studentTerm.hasPassed(courseSection.getCourse())))
            throw new AlreadyPassedCourseException(courseSection.getCourse());
    }

    private void checkForGpaLimit(List<CourseSection> courseSections, Student student) throws EnrollmentRulesViolationException {
        if ((student.getGpa() < ACADEMIC_PROBATION_GRADE_LIMIT && sumUnits(courseSections) > ACADEMIC_PROBATION_UNITS_LIMIT) ||
                (student.getGpa() < DISTINGUISHED_GRADE_LIMIT && sumUnits(courseSections) > NORMAL_STUDENT_UNITS_LIMIT) ||
                (sumUnits(courseSections) > DISTINGUISHED_STUDENT_UNITS_LIMITS))
            throw new GpaLimitException(sumUnits(courseSections), student.getGpa());
    }

    private int sumUnits(List<CourseSection> courseSections) {
        return courseSections.stream().mapToInt(courseSection -> courseSection.getCourse().getUnits()).sum();
    }

}
