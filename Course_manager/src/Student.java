
public class Student {
	private String name;
	private String password;
	private String username;
	private int classCount = 0;
	
	public Student(String name, String password, String username) {
		this.name = name;
		this.username = username;
		this.password = password;
	}
	
	public Student() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getClassCount() {
		return classCount;
	}

	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}
	
	public void incrementClassCount() {
		classCount ++;
	}
	
	public void decrementClassCount() {
		if (classCount != 0) {
			classCount --;
		}
	}

}
