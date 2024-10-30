
public class Course {
	
	private String subject ;
	private String teacher ;
	private String code ;
	private double grade;
	private boolean display;
	private int classNumber;
	private int assignmentCount;
	private double gradeSum;
	
	public Course (int classNumber, String subject, String teacher, String code, double grade, int assignmentCount) {
		this.classNumber = classNumber;
		this.subject = subject;
		this.teacher = teacher;
		this.code = code;
		this.grade = grade;
		this.assignmentCount = assignmentCount;
		this.gradeSum = grade * ( assignmentCount+1 );
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}


	public int getAssignmentCount() {
		return assignmentCount;
	}


	public void setAssignmentCount(int assignmentCount) {
		this.assignmentCount = assignmentCount;
	}
	
	public void incrementAssignmentCount() {
		assignmentCount ++;
	}
	
	public void derementAssignmentCount() {
		if (assignmentCount != 0) {
			assignmentCount --;	
		}
	}
	
	public void addGradeSum(double grade) {
		this.gradeSum += grade;
	}


	public double getGradeSum() {
		return gradeSum;
	}


	public void setGradeSum(double gradeSum) {
		this.gradeSum = gradeSum;
	}
	
	public void averageGrade() {
		grade = gradeSum / (assignmentCount + 1);
	}
	
	
	
	

}
