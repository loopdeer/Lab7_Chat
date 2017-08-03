package assignment7;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.io.PrintWriter;
import java.util.Scanner;

import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;

import javafx.event.Event;

public class ClientMainController {
	@FXML
	private TextArea viewArea;
	@FXML
	private TextField messageBox;
	@FXML
	private TextField groupTextField;
	@FXML
	private Button sendButton;
	@FXML
	private TextArea viewAreaGroup;
	@FXML
	private Button addressButton;
	@FXML
	private TextField addressBox;
	@FXML
	private Button groupSendButtton;
	
	private TextArea mainArea;
	private PrintWriter writer;
	private TextField mainTextField;
	public ClientMainController(PrintWriter w)
	{
		writer = w;
		mainArea = viewArea;
		mainTextField = messageBox;
	}
	// Event Listener on Tab.onSelectionChanged
	@FXML
	public void allSelected(Event event) {
		// TODO Autogenerated
		mainArea = viewArea;
		mainTextField = messageBox;
	}
	// Event Listener on Button[#sendButton].onAction
	@FXML
	public void sendMessage(ActionEvent event) {
		// TODO Autogenerated
		//String test = mainTextField.getText();
		Scanner commandChecker = new Scanner(mainTextField.getText());
		if(commandChecker.next().equals(ChatClient.COMMANDSTART))
		{
			mainArea.appendText("Do not start messages with " + ChatClient.COMMANDSTART + "\n");
			return;
		}
		
		writer.println(mainTextField.getText());
		writer.flush();
		mainTextField.setText("");
		mainTextField.requestFocus();
	}
	// Event Listener on Tab.onSelectionChanged
	@FXML
	public void groupSelected(Event event) {
		// TODO Autogenerated
		mainArea = viewAreaGroup;
		mainTextField = groupTextField;
	}
	// Event Listener on Button[#addressButton].onAction
	@FXML
	public void talkTo(ActionEvent event) {
		// TODO Autogenerated
		
		//TODO check for proper syntax
		String people = addressBox.getText();
		addressBox.setText("");
		addressBox.requestFocus();
		writer.println(ChatClient.COMMANDSTART + " startconv " + people);
		writer.flush();
	}
	
	public TextArea getTA()
	{
		return mainArea;
	}
}
