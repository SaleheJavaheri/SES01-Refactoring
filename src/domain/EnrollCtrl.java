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
        List<Course> prereqs = courseSection.getCourse().getPrerequisites();
        nextPre:
        for (Course pre : prereqs) {
            for (Map.Entry<Term, StudentTerm> tr : student.getTerms().entrySet()) {
                for (Map.Entry<Course, Double> r : tr.getValue().getGrades().entrySet()) {
                    if (r.getKey().equals(pre) && r.getValue() >= 10)
                        continue nextPre;
                }
            }
            throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.toString(), courseSection.getCourse().getName()));
        }
    }

    private void checkForAlreadyPassedCourse(CourseSection o, Student student) throws EnrollmentRulesViolationException {
        for (Map.Entry<Term, StudentTerm> tr : student.getTerms().entrySet()) {
            for (Map.Entry<Course, Double> r : tr.getValue().getGrades().entrySet()) {
                if (r.getKey().equals(o.getCourse()) && r.getValue() >= 10)
                    throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
            }
        }
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
