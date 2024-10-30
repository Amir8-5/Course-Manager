import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * An application that allows the users to login, enter their grades, and save the information in a file to be viewed later. Basically a grader applcation.
 * @author Amir Bouryazadeh - 350112918
 * @since 12/30/20222
 */
public class Main extends Application {
	//All variables that need to be accessed
	static Student student = new Student();
	static String filePath;
	static Stage window = new Stage();
	static Scene mainScene;
	static Scene loginScene;
	static Scene signUpScene;
	static Scene classScene;
	static VBox classVBox;
	static ArrayList<Course> courseArray = new ArrayList<>();
	static ArrayList<Assignment> assignmentArray = new ArrayList<Assignment>();
	static double average = 0;
	static double gradeSum = 0;
	static ProgressIndicator averageIndicator = new ProgressIndicator(average);
	static String courseContent = "";
	static VBox assignmentContainer = new VBox(20);
	static ProgressIndicator assignmentIndicator = new ProgressIndicator(0);
	static String assignmentContent = "";

	/**
	 * A method that reads information from a file and fills up the assignmentArray 
	 * @param pre : There should be a file for that classes assignments made beforehand
	 * @param post : assignmentArray should be filled with all the information in the file
	 */
	public static void fillAssignment(int classNumber) {
		String path = student.getUsername() + "-class-" + classNumber + "-assignments.txt";
		File file = new File(path);
		if ( file.exists()) {
			BufferedReader reader = null;
			String oldContent = "";
			String line = "";
			int assignmentCount = 0;
			try {
				reader = new BufferedReader(new FileReader(file));
				for (int i = 0; i < 2; i ++) {
					line = reader.readLine();
					if (line.contains("assignment-count:")) {
						assignmentCount = Integer.parseInt(line.replace("assignment-count:", ""));
					}
					oldContent += line;
				}
				for (int i = 0; i < assignmentCount; i ++) {
					int assignmentNumber = Integer.parseInt(reader.readLine().replace("assignment-number:", ""));
					String name = reader.readLine().replace("name:", "");
					double grade = Double.parseDouble(reader.readLine().replace("grade:", ""));
					Assignment assignment = new Assignment(name, grade, classNumber, assignmentNumber);
					assignmentArray.add(assignment);
				}
				reader.close();


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("There was an error in fillAssignment");
			}
		}
	}
	
	/**
	 * A method that writes all the contents of the assignmentArray to a file
	 * @param pre : There should be assignments and a file made before
	 * @param post : All the contents of the assignmentArray should be written to the file
	 */
	public static void writeAssignment(int classNumber) {

		assignmentArray.forEach(e -> {
			if (e.getClassNumber() == classNumber) {
				assignmentContent += "assignment-number:" + e.getAssignmentNumber() + "\nname:" + e.getName() + "\n" + "grade:" + e.getGrade() + "\n";
				String path = student.getUsername() + "-class-" + e.getClassNumber() + "-assignments.txt";
				final int INDEX = e.getClassNumber()-1;
				File file = new File(path);
				FileWriter writer = null;
				try {
					if (! file.exists()) {
						file.createNewFile();
					}
					PrintWriter pw = new PrintWriter(file);
					pw.close();
					writer = new FileWriter(file.getPath(), true);
					writer.write(
							"assignment-count:" + courseArray.get(INDEX).getAssignmentCount() + "\n" +
									"assignment:\n" + assignmentContent + "\n"
							);
					writer.close();

				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("There was an error in writeAssignment");
				}
			}
		});
		assignmentContent = "";
	}

	/**
	 * A method puts all the assignments in the class into a container 
	 * @param pre : There should be a class and assignments made in the class
	 * @param post : All the assignmetns for that class should be put into the container
	 */
	public static void showAssignment(int classNumber) {
		//Adding elements to the container 
		assignmentArray.forEach(e -> {
			if (e.getClassNumber() == classNumber) {
				final int INDEX = classNumber - 1;
				String name = e.getName();
				double grade = e.getGrade();

				Label nameLabel = new Label("Assignment: " + name);
				StackPane assignmentPane = new StackPane();
				assignmentPane.getStyleClass().add("new-class-shape");

				ProgressIndicator pi = new ProgressIndicator(grade/100);
				pi.setMinHeight(110);
				pi.setMinWidth(110);
				pi.getStyleClass().add("class-progress-indicator");
				pi.setPadding(new Insets(20, 0, 20, 20));

				HBox showAssignmentLayout = new HBox();
				showAssignmentLayout.getChildren().addAll(pi, nameLabel);
				showAssignmentLayout.setAlignment(Pos.CENTER);
				showAssignmentLayout.setSpacing(210);

				assignmentPane.getChildren().add(showAssignmentLayout);
				assignmentContainer.getChildren().add(assignmentPane);
			}
		});
	}
	/**
	 * A method that makes a new object assignment and adds it ot the assignmentArray
	 * @param pre : There has to be a class made for the assignment you are trying to make
	 * @param post : There should be a new object in the assignmentArray with all the information given
	 */
	public static void newAssignment(int classNumber) {
		final int INDEX = classNumber - 1;
		courseArray.get(INDEX).incrementAssignmentCount();
		//TextFields
		TextField nameText = new TextField();
		nameText.setPromptText("Assignment name");
		nameText.setFocusTraversable(false);

		TextField gradeText = new TextField();
		gradeText.setPromptText("Grade");
		gradeText.setFocusTraversable(false);

		//Button
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			courseArray.get(INDEX).derementAssignmentCount();
			assignmentArray.forEach(e1 -> {
				e1.setDisplay(false);
			});
			openClassScene(classNumber);
			nameText.clear();
			gradeText.clear();
		});

		Button newAssignmentButton = new Button("Create Class");
		newAssignmentButton.requestFocus();
		newAssignmentButton.setFocusTraversable(true);
		newAssignmentButton.setOnAction(e -> {
			//Making variables for the text
			String name = nameText.getText();
			nameText.clear();

			double grade = Double.parseDouble(gradeText.getText());
			gradeText.clear();

			Assignment assignment = new Assignment(name, grade, classNumber);
			assignmentArray.add(assignment);
			showAssignment(classNumber);

			courseArray.get(INDEX).addGradeSum(grade);
			courseArray.get(INDEX).averageGrade();
			assignmentIndicator.setProgress(courseArray.get(INDEX).getGrade()/100);

			openClassScene(classNumber);
		});

		//Button layout
		HBox box = new HBox(10);
		box.getChildren().addAll(newAssignmentButton, backButton);

		//Layout
		VBox root = new VBox(20);
		root.getChildren().addAll(nameText, gradeText, box);
		root.setAlignment(Pos.BASELINE_CENTER);
		//Scene
		Scene newAssignmentScene = new Scene(root, 1000, 800);
		newAssignmentScene.getStylesheets().add("StyleSheet2.css");

		//window
		window.setScene(newAssignmentScene);
	}
	/**
	 * A method that opens a the scene for the class which was selected by the user
	 * @param pre : There must be a class made before for the one trying to open
	 * @param post : There should be a new scene opnened with all the assigments for the class displayed
	 */
	public static void openClassScene(int classNumber) {
		//Clearning other classes contents from the container
		assignmentContainer.getChildren().clear();

		//Filling the assignment container with all the assignments made for this class
		showAssignment(classNumber);
		final int INDEX = classNumber - 1;

		//Progress Indicator
		assignmentIndicator.setProgress(courseArray.get(INDEX).getGrade()/100);
		assignmentIndicator.setMinHeight(150);
		assignmentIndicator.setMinWidth(150);

		//Making back button
		Button backButton = new Button("back");
		backButton.setMinHeight(50);
		backButton.setMinHeight(50);
		backButton.setOnAction(e -> {
			window.setScene(mainScene);
		});

		//Progress indicator layout
		VBox progressVBox = new VBox(10);
		Label progressLabel = new Label("Your Average:");
		progressLabel.getStyleClass().add("label1");
		progressVBox.getChildren().addAll(progressLabel, assignmentIndicator);  
		progressVBox.setAlignment(Pos.TOP_CENTER);
		progressVBox.setPadding(new Insets(20));


		BorderPane progressBorder = new BorderPane();
		progressBorder.setCenter(progressVBox);
		progressBorder.setLeft(backButton);
		progressBorder.setPadding(new Insets(20, 0 ,0 , 20));

		//Making an assignment shape
		Label newAssignmentLabel = new Label("Add Assignment");
		StackPane newAssignmentPane = new StackPane();
		newAssignmentPane.getStyleClass().add("new-class-shape");
		newAssignmentPane.getChildren().add(newAssignmentLabel);
		newAssignmentPane.setOnMouseClicked(e -> {
			newAssignment(classNumber);
			assignmentArray.forEach(e1 -> e1.setDisplay(false));
		});

		newAssignmentPane.setAlignment(Pos.CENTER);
		HBox addAssignmentHBox = new HBox(newAssignmentPane); 
		addAssignmentHBox.setAlignment(Pos.BOTTOM_CENTER);
		HBox.setMargin(newAssignmentPane, new Insets(20));

		//Layout
		BorderPane root = new BorderPane();
		root.setTop(progressBorder);
		root.setBottom(addAssignmentHBox);

		//Showing all the classes in scrollable container

		//Vbox for the classes
		VBox assignmentBox = new VBox(40);
		assignmentBox.setAlignment(Pos.CENTER);
		assignmentBox.setPadding(new Insets(10, 50 , 10, 40));

		//Scroll pane
		ScrollPane scroll = new ScrollPane();
		root.setCenter(scroll);

		scroll.setContent(assignmentContainer);
		assignmentContainer.setPadding(new Insets(10, 50 , 10, 40));

		//Scene
		classScene = new Scene(root, 1000, 800);
		classScene.getStylesheets().add("StyleSheet2.css");
		window.setScene(classScene);
	}

	/**
	 * A method that reads from a file and puts the course information from the file into the courseArray
	 * @param pre : There should be course information written in the file
	 * @param post : courseArray should have the amount of courses that are in the file as course objects
	 */
	public static void fillCourseArray() {
		File file = new File(filePath);
		BufferedReader reader = null;
		String line = "";
		int classCounter = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			while (line != null) {
				if (line.contains("class-count=")) {
					classCounter = Integer.parseInt(line.replace("class-count=", ""));
				}
				if (line.equals("classes:")) {
					for (int i = 0 ; i < classCounter; i++) {
						int classNumber = Integer.parseInt(reader.readLine().replace("class-number:", ""));
						String subject = reader.readLine().replace("subject:", "");
						String teacher = reader.readLine().replace("teacher:", "");
						String code = reader.readLine().replace("code:", "");
						double grade = Double.parseDouble(reader.readLine().replace("grade:", ""));
						int assignmentCount = Integer.parseInt(reader.readLine().replace("assignment-count:", ""));
						courseArray.add(new Course(classNumber, subject, teacher, code, grade, assignmentCount));
					}
				}
				line = reader.readLine();
			}

		} catch (Exception e) {
			System.out.println("There was an error in fillCourseArray");
			e.printStackTrace();
		}
	}

	/**
	 * A method that loops through the courses and puts them all in a format in a string
	 * @param pre : There should be classes registered
	 * @param post : There should be a string returned with all the classes in correct format
	 */

	public static void writeContent() {
		courseArray.forEach(e -> {
			String count = "class-number:" + e.getClassNumber();
			String subject = "subject:" + e.getSubject();
			String teacher = "teacher:" + e.getTeacher();
			String code = "code:" + e.getCode();
			String grade = "grade:" + e.getGrade();
			String assignmentCount = "assignment-count:" + e.getAssignmentCount();
			String content = count + "\n" + subject + "\n" + teacher + "\n" + code + "\n" + grade + "\n" + assignmentCount + "\n"; 
			if (!(courseContent.contains(content))) {
				courseContent += content;
			}
		});
	}
	/**
	 * A method that inputs all the information from the app into a file
	 * @param pre : There should be information to input into the file
	 * @param post : The file should have all the information written into it
	 */
	public static void endApp() {
		refreshClassCount();

		//Calling the method for each class
		for (int i = 1; i < 9; i++) {
			writeAssignment(i);	
		}

		BufferedReader reader = null;
		FileWriter writer = null;
		String oldContent = "";
		String line;

		try {
			File file = new File(filePath);
			reader = new BufferedReader(new FileReader(file));
			for (int i = 0 ; i < 6 ; i++) {
				line = reader.readLine();
				oldContent = oldContent + line + System.lineSeparator();
			}
			writeContent();
			String newContent = oldContent + courseContent;
			PrintWriter pw = new PrintWriter(file);
			pw.close();
			writer = new FileWriter(file.getPath(), true);
			writer.write(newContent);
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("There was an error in endApp");
		}
		window.close();

	}

	/**
	 * A method that sets the variable classCount to the number set by the file 
	 * @param pre:The user should have signed up
	 * @param post: The variable classCount should be set to the number in the file
	 */
	public static void setClassCount() {
		File file = new File(filePath);
		BufferedReader reader = null;
		String line;
		int num;
		try {
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			while (line != null) {
				if (line.contains("class-count=")) {
					num = Integer.parseInt(line.replace("class-count=", ""));
					student.setClassCount(num);
				}
				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("There was an error in setClassCount");
		}
	}
	/**
	 * A method that average the sum of all grades by dividing it by the class count
	 * @param pre : There should be classes made so that there are marks 
	 * @param post: The variable average should be set to the average of the grades
	 */
	public static void setAverage(double grade) {
		gradeSum += grade;
		System.out.println(gradeSum);
		System.out.println(courseArray.size());
		average = gradeSum / courseArray.size();
		averageIndicator.setProgress(average / 100);
	}

	/**
	 * A method for updating the value of classCount in the file to the one from the global variable
	 *@param pre : The user should have signed up so there is a file to read from
	 * @param post: The variable classCount should have been updated to the value set in the file
	 */
	public static void refreshClassCount() {
		File file = new File(filePath);
		BufferedReader reader = null;
		FileWriter writer = null;
		String line;
		String content = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			while (line != null) {
				if (line.contains("class-count=")) {
					line = "class-count=" + student.getClassCount();
				}
				content = content + line + System.lineSeparator();
				line = reader.readLine();
			}
			PrintWriter pw = new PrintWriter(file);
			pw.close();
			writer = new FileWriter(file.getPath(), true);
			writer.write(content);
			writer.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("There was an error in refreshClassCount");
		}
	}

	/**
	 * A method that takes in the information of the user and writes all the information to a file
	 * @param pre:The textfields have to be filled to get information
	 * @param post: A file should be created with the user information stored inside
	 */
	public static void userInfo(String name , String user, String pass) {
		try {
			System.out.println(user);
			File userInfo = new File(user + ".txt");
			System.out.println(userInfo.getPath());
			if (userInfo.getPath().equals(".txt")) {
				AlertBox.display("Error", "The username may already be taken or the field has not been filled");
			} else if (userInfo.exists()) {
				AlertBox.display("Error", "This username is taken");
			}
			FileWriter writer = new FileWriter(userInfo.getPath(), true);
			if (userInfo.length() == 0) {
				writer.write("user-information:" + System.lineSeparator());
				writer.write("user:" + user + System.lineSeparator());
				writer.write("username:" + user + "," + "password:" + pass + System.lineSeparator());
				writer.write("name:" + name + System.lineSeparator());
				writer.write("class-count=0" + System.lineSeparator());
				writer.write("classes:" + System.lineSeparator());
				writer.close();		
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("An Error occured in userInfo");
		}
	}

	/**
	 * A method that checks if a username and password given matches the one in a file and returns a boolean value
	 * @param pre-condition: The username and password should not be a duplicate
	 * @param post-condition : There will be a boolean value returned which shows whether the username and password are correct and exist in file
	 */
	public static boolean loginCheck(String username, String password) {
		boolean verify = false;
		try {
			File userInfo = new File(username + ".txt");
			if (!userInfo.exists()) {
				userInfo.createNewFile();
			}
			String user = "username:" + username;
			String pass = "password:" + password;
			Scanner sc = new Scanner(userInfo);
			while(sc.hasNextLine() ) {
				if (sc.nextLine().equals(user + "," + pass)) {
					verify = true;
				}

			}
			sc.close();

		} catch (IOException e) {
			System.out.println("Could not locate the file");
			AlertBox.display("Error", "The username and passwrod could not be found. You have to sign-up if you do not have one.");
			System.out.println("There was an error in loginCheck");
		}
		return verify;
	}



	/**
	 * A method that creates a new class by taking in the information that the user gives and writes it in a file to be used later
	 * @param pre:The string given for the grade should be a number
	 * @param post: All the information given should be written in a file
	 */

	public static void newClass() {
		student.incrementClassCount();;
		if (student.getClassCount() <= 8) {
			//TextFields
			TextField teacherText = new TextField();
			teacherText.setPromptText("Teacher name");
			teacherText.setFocusTraversable(false);


			TextField subjectText = new TextField();
			subjectText.setPromptText("Subject");
			subjectText.setFocusTraversable(false);


			TextField classCodeText = new TextField();
			classCodeText.setPromptText("Class code");
			classCodeText.setFocusTraversable(false);


			TextField gradeText = new TextField();
			gradeText.setPromptText("Grade");
			gradeText.setFocusTraversable(false);

			//Button
			Button backButton = new Button("Back");
			backButton.setOnAction(e -> {
				student.decrementClassCount();
				window.setScene(mainScene);
			});

			Button newClassButton = new Button("Create Class");
			newClassButton.requestFocus();
			newClassButton.setFocusTraversable(true);
			newClassButton.setOnAction(e -> {
				//Making variables for the text
				String teacher = teacherText.getText();
				teacherText.clear();

				String subject = subjectText.getText();
				subjectText.clear();

				String classCode = classCodeText.getText();
				classCodeText.clear();

				double grade = Double.parseDouble(gradeText.getText());
				gradeText.clear();

				Course course = new Course(student.getClassCount(), subject, teacher, classCode, grade,0);
				courseArray.add(course);

				window.setScene(mainScene);
				showClass();
			});

			//Button layout
			HBox box = new HBox(10);
			box.getChildren().addAll(newClassButton, backButton);

			//Layout
			VBox root = new VBox(20);
			root.getChildren().addAll(teacherText, subjectText, classCodeText, gradeText, box);
			root.setAlignment(Pos.BASELINE_CENTER);
			//Scene
			Scene newClassScene = new Scene(root, 1000, 800);

			//window
			window.setScene(newClassScene);
		}
		else {
			AlertBox.display("Error", "You cannot have more than 8 classes");
		}
	}


	/**
	 * A method that creates a class shape with the information stored in the file
	 * @param pre: There has to be a information inside the file to create the class
	 * @param post: There should be a hollow rectangle showing all the information of the class
	 */
	public static void showClass() {
		courseArray.forEach(e -> {
			if (! e.isDisplay()) {

				String subject = e.getSubject();
				String teacher = e.getTeacher();
				String code = e.getCode();
				double grade = e.getGrade();
				setAverage(grade);

				StackPane classPane = new StackPane();
				classPane.getStyleClass().add("new-class-shape");
				classPane.setOnMouseClicked(e1 -> openClassScene(e.getClassNumber()));

				Label subjectLabel = new Label("Subject:  " + subject);
				subjectLabel.getStyleClass().add("new-class-label");

				Label teacherLabel = new Label("Teacher:  " + teacher);
				teacherLabel.getStyleClass().add("new-class-label");

				Label classCodeLabel = new Label("Class code:  " + code);
				classCodeLabel.getStyleClass().add("new-class-label");

				ProgressIndicator pi = new ProgressIndicator(grade/100);

				pi.setMinHeight(110);
				pi.setMinWidth(110);
				pi.getStyleClass().add("class-progress-indicator");
				pi.setPadding(new Insets(20, 0, 20, 20));

				VBox textBox = new VBox(10);
				textBox.getChildren().addAll(subjectLabel, teacherLabel, classCodeLabel);
				HBox showClassLayout = new HBox();
				showClassLayout.getChildren().addAll(pi, textBox);
				textBox.setAlignment(Pos.CENTER_RIGHT);
				showClassLayout.setSpacing(210);

				classPane.getChildren().add(showClassLayout);
				classVBox.getChildren().add(classPane);
				e.setDisplay(true);			
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Stage
		window = new Stage();
		window.setTitle("Grader");
		//Uncomment this later
		window.setScene(loginScene);
		


		/**
		 * Making the login section
		 */


		//Username text
		TextField userText = new TextField();
		userText.setFocusTraversable(false);
		userText.setPromptText("username");

		//Password Components 
		//Password Label

		//Password field
		PasswordField passField = new PasswordField();
		passField.setPromptText("password");
		passField.setFocusTraversable(false);

		//Password text
		TextField passText = new TextField();
		passText.setPromptText("password");
		passText.setVisible(false);

		//Password Checkbox
		CheckBox passBox = new CheckBox("Show Password");

		//Password Checkbox Action
		passBox.setVisible(true);
		passBox.setOnAction(e -> {
			if (passBox.isSelected()) {
				passText.setText(passField.getText());
				passField.visibleProperty().set(false);
				passText.visibleProperty().set(true);
			} else {
				passField.setText(passText.getText());
				passField.visibleProperty().set(true);
				passText.visibleProperty().set(false);
			}
		});


		//Login Button
		Button loginButton = new Button("Login");

		//Login Button Actions 
		loginButton.setOnAction(e -> {
			String user = userText.getText();
			String pass = passField.getText();
			if (loginCheck(user, pass)) {
				window.setOnCloseRequest(e1 -> endApp()); 
				window.setTitle("Main Screen");
				student.setUsername(user);
				student.setPassword(pass);
				filePath = student.getUsername() + ".txt";
				fillCourseArray();
				for (int i = 1; i < 9; i++) {
					fillAssignment(i);
				}
				setClassCount();
				showClass();
				window.setScene(mainScene);

			} else {
				AlertBox.display("Error", "Username or password was invalid");
				passField.clear();
				userText.clear();
				passText.clear();
				passBox.setSelected(false);
			}
		});

		//Sign-up button
		Button signUpButton = new Button("Sign-up");
		signUpButton.setOnAction(e -> {
			userText.clear();
			passField.clear();
			passField.setVisible(true);
			passText.clear();
			passText.setVisible(false);
			passBox.setSelected(false);
			window.setScene(signUpScene);
			window.setTitle("Sign-Up");
		});

		//Exit button
		Button exitButton = new Button("EXIT");
		exitButton.setOnAction(e -> window.close());
		exitButton.getStyleClass().add("exit-button");

		//Layout
		GridPane loginRoot = new GridPane();
		loginRoot.getChildren().addAll(userText, passField, loginButton, signUpButton, passBox, passText, exitButton);

		//Layout Children Location
		GridPane.setConstraints(userText, 0, 1);
		GridPane.setConstraints(passField, 0, 2);
		GridPane.setConstraints(passText, 0, 2);
		GridPane.setConstraints(passBox, 1, 2);
		GridPane.setConstraints(loginButton, 0, 3);
		GridPane.setConstraints(signUpButton, 0, 4);
		GridPane.setConstraints(exitButton, 0, 5);

		//Layout spacing
		loginRoot.setVgap(40);
		loginRoot.setHgap(20);
		loginRoot.setPadding(new Insets(20));
		loginRoot.requestFocus();
		loginRoot.setAlignment(Pos.BASELINE_CENTER);

		//Scene
		loginScene = new Scene(loginRoot, 1000, 800);
		loginScene.getStylesheets().add("StyleSheet2.css");
		window.setScene(loginScene);
		window.show();


		/**
		 * Making a section for sign-up
		 */

		//Name text
		TextField signNameText = new TextField();	
		signNameText.setPromptText("name");
		signNameText.setFocusTraversable(false);

		//Username text
		TextField signUserText = new TextField();		
		signUserText.setPromptText("username");
		signUserText.setFocusTraversable(false);

		//Password field
		PasswordField signPassField = new PasswordField();	
		signPassField.setPromptText("password");
		signPassField.setFocusTraversable(false);

		//Password textfield
		TextField signPassText = new TextField();
		signPassText.setPromptText("password");
		signPassText.visibleProperty().set(false);

		//Password Checkbox
		CheckBox signPassBox = new CheckBox("Show Password");


		//Password Checkbox Action
		signPassBox.setOnAction(e -> {
			if (signPassBox.isSelected()) {
				signPassText.setText(signPassField.getText());
				signPassField.visibleProperty().set(false);
				signPassText.setVisible(true);
			} else {
				signPassField.setText(signPassText.getText());
				signPassField.visibleProperty().set(true);
				signPassText.setVisible(false);
			}
		});		

		//Button
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			window.setScene(loginScene);
			signNameText.clear();
			signUserText.clear();
			signPassField.clear();
			signPassText.clear();
			signPassBox.setSelected(false);
		});
		Button signButton = new Button("Sign-up");
		signButton.setOnAction(e -> {
			student.setName(signNameText.getText());
			student.setUsername(signUserText.getText());
			student.setPassword(signPassField.getText());
			signNameText.clear();
			signUserText.clear();
			signPassField.clear();
			signPassText.clear();
			signPassBox.setSelected(false);
			userInfo(student.getName(), student.getUsername(), student.getPassword());
			window.setScene(loginScene);
		});

		//Layout & Children
		GridPane signUpLayout = new GridPane();
		signUpLayout.getChildren().addAll( signNameText, signUserText, signPassField, signButton, signPassBox, signPassText, backButton);

		//Layout location
		GridPane.setConstraints(signNameText, 1, 0);
		GridPane.setConstraints(signUserText, 1, 1);
		GridPane.setConstraints(signPassField, 1, 2);
		GridPane.setConstraints(signPassText, 1, 2);
		GridPane.setConstraints(signPassBox, 2, 2);
		GridPane.setConstraints(signButton, 1, 3);
		GridPane.setConstraints(backButton, 2, 3);

		//Layout spacing
		signUpLayout.setPadding(new Insets(20));
		signUpLayout.setVgap(20);
		signUpLayout.setHgap(20);

		//Scene
		signUpScene = new Scene(signUpLayout, 1000, 800);
		signUpScene.getStylesheets().add("StyleSheet2.css");

		/**
		 * Making a main menu for the application
		 */

		//Progress indicator properties
		averageIndicator.setMinHeight(150);
		averageIndicator.setMinWidth(150);

		//Progress indicator layout
		VBox progressVBox = new VBox(10);
		Label progressLabel = new Label("Your Average:");
		progressLabel.getStyleClass().add("label1");
		progressVBox.getChildren().addAll(progressLabel, averageIndicator);  
		progressVBox.setAlignment(Pos.TOP_CENTER);
		progressVBox.setPadding(new Insets(20));


		//Making a class

		//Making a class shape
		Label newClassLabel = new Label("Add Class");
		StackPane newClassPane = new StackPane();
		newClassPane.getStyleClass().add("new-class-shape");
		newClassPane.getChildren().add(newClassLabel);
		newClassPane.setOnMouseClicked(e -> {
			newClass();
		});
		newClassPane.setAlignment(Pos.CENTER);
		HBox addClassHBox = new HBox(newClassPane);
		addClassHBox.setAlignment(Pos.BOTTOM_CENTER);
		HBox.setMargin(newClassPane, new Insets(20));

		//Layout
		BorderPane root = new BorderPane();
		root.setTop(progressVBox);
		root.setBottom(addClassHBox);

		//Showing all the classes in scrollable container

		//Vbox for the classes
		classVBox = new VBox(40);
		classVBox.setAlignment(Pos.CENTER);
		classVBox.setPadding(new Insets(10, 50 , 10, 40));
		//Scroll pane
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(classVBox);
		root.setCenter(scroll);
		//Scene
		mainScene = new Scene(root, 1000, 800);
		mainScene.getStylesheets().add("StyleSheet2.css");

		//Window
		window.show();
	}
}
