package domain;

import domain.StudentTerm;
import domain.Term;

import java.util.HashMap;
import java.util.Map;

public class TermManger {
    private Map<Term, StudentTerm> terms = new HashMap<>();


    public Map<Term, StudentTerm> getTerms() {
        return terms;
    }

    public void addCourseGrade(Course course, Term term, double grade) {
        if (!terms.containsKey(term))
            terms.put(term, new StudentTerm());
        terms.get(term).addGrade(course, grade);
    }
}
