package domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Course {
	private String id;
	private String name;
	private int units;
	
	private List<Course> prerequisites;

	public Course(String id, String name, int units) {
		this.id = id;
		this.name = name;
		this.units = units;
		prerequisites = new ArrayList<>();
	}
	
	public void addPrerequisite(Course course) {
		prerequisites.add(course);
	}

	public void addPrerequisite(Course... courses) {
		this.prerequisites.addAll(Arrays.asList(courses));
	}

	public List<Course> getPrerequisites() {
		return prerequisites;
	}

	public String getPrerequisitesToString() {
		return String.format("{ %s }",
				prerequisites.stream().map(Course::getName).collect(Collectors.joining(", ")));
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return String.format("%s %s", name, getPrerequisitesToString());
	}

	public int getUnits() {
		return units;
	}

	public String getId() {
		return id;
	}

	public boolean equals(Object obj) {
		return obj instanceof Course && id.equals(((Course) obj).id);
	}
}
