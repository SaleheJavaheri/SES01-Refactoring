package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
    public void enroll(Student student, List<CourseSection> courses) throws EnrollmentRulesViolationException {
        Map<Term, StudentTerm> transcript = student.getTerms();

        for (CourseSection o : courses) {
            checkForAlreadyPassedCourse(o, student);
            checkForPrerequisites(o, student);
            for (CourseSection o2 : courses) {
                if (o == o2)
                    continue;
                if (o.getExamTime().equals(o2.getExamTime()))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                if (o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        }
        checkForGpaLimit(courses, student);
        for (CourseSection courseSection : courses)
            student.takeCourseSection(courseSection);
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
