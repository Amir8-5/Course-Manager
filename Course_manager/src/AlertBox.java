import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static void display(String title, String message) {
		Stage window1 = new Stage();
		window1.setTitle(title);
		window1.initModality(Modality.APPLICATION_MODAL);
		
		Button button = new Button("Click to close");
		button.setOnAction(e -> {
			window1.close();
		});
		
		Label label = new Label();
		label.getStyleClass().add("alert-label");
		label.setText(message);
		
		
		VBox layout = new VBox(40);
		layout.getChildren().addAll(label, button);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 600, 600);
		scene.getStylesheets().add("StyleSheet2.css");
		
		window1.setScene(scene);
		window1.showAndWait();
		
		
	}

}
