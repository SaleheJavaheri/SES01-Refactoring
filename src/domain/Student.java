package domain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Student {
	private String id;
	private String name;
	private TermManger termManger;
	private List<CourseSection> currentTerm;

	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		this.termManger = new TermManger();
		this.currentTerm = new ArrayList<>();
	}
	
	public void takeCourseSection(List<CourseSection> courseSection) {
		currentTerm.addAll(courseSection);
	}

	public Map<Term, StudentTerm> getTerms() {
		return termManger.getTerms();
	}

	public void addCourseGrade(Course course, Term term, double grade) {
	    termManger.addCourseGrade(course, term, grade);
    }

    public List<CourseSection> getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public double getGpa() {
		double points = getTerms().values().stream().mapToDouble(StudentTerm::getPoint).sum();
		int totalUnits = getTerms().values().stream().mapToInt(StudentTerm::getSumUnits).sum();
		return points / totalUnits;
	}
}
