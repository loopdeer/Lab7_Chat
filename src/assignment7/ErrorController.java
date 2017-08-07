package assignment7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
	@FXML
	private Label errorLabel;
	@FXML
	private Button okButton;
	
	private Stage stage;
	
	public ErrorController(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	public void closeWindow(ActionEvent event) {
		stage.close();
	}
	
	public Label getLabel() {
		return errorLabel;
	}
}
