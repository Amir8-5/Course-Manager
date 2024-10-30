
public class Assignment {
	
	private String name ;
	private double grade ;
	private int classNumber;
	private boolean display;
	private int assignmentNumber;
	
	public Assignment (String name, double grade, int classNumber) {
		this.name = name;
		this.grade = grade;
		this.classNumber = classNumber;
		assignmentNumber = Main.courseArray.get(classNumber-1).getAssignmentCount();
	}
	public Assignment (String name, double grade, int classNumber, int assignmentNumber) {
		this.name = name;
		this.grade = grade;
		this.classNumber = classNumber;
		this.assignmentNumber = assignmentNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public int getClassNumber() {
		return classNumber;
	}
	
	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}
	

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public int getAssignmentNumber() {
		return assignmentNumber;
	}

	public void setAssignmentNumber(int assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}

	
	

}
