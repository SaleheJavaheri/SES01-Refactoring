package domain.exceptions;

public class GpaLimitException extends EnrollmentRulesViolationException {
    public GpaLimitException(int totalUnits, double studentGpa) {
        super(String.format("Number of units (%d) requested does not match GPA of %f", totalUnits, studentGpa));
    }
}
