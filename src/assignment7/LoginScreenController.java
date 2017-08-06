package assignment7;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class LoginScreenController {
	@FXML
	private TextField userField;
	@FXML
	private Button enterButton;
	@FXML
	private Label welcomeLabel;
	@FXML
	private Label userLabel;
	
	public static Set<String> users;
	private String un;
	private Stage st;
	private static ClientMainController cc;
	private static TextArea incoming;
	private static PrintWriter writer;
	
	public LoginScreenController(Stage st, PrintWriter writer) {
		this.st = st;
		//this.cc = cc;
		//this.incoming = incoming;
		this.writer = writer;
		//System.out.println(writer);
	}
	
	@FXML
	public void createUser(ActionEvent event) throws IOException {
		String username = userField.getText();
		/*if (users.contains(username)) {
			Stage stage = new Stage();
			FXMLLoader ldr = new FXMLLoader();
            ldr.setLocation(ClientMain.class.getResource("Error.fxml"));
            //LoginScreenController lsc = new LoginScreenController();
            ErrorController ec = new ErrorController(stage);
            ldr.setController(ec);
            TitledPane tp = (TitledPane) ldr.load();
            Scene error = new Scene(tp);
            Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					stage.setScene(error);
					stage.show();
				}
            	
            });
		}*/
			//users.add(username);
			un = username;
			ChatClient test = new ChatClient();
			try {
				writer = test.run(un);
				//System.out.println(writer);
				FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(ClientMain.class.getResource("ClientMain.fxml"));
	            cc = new ClientMainController(writer);
	            loader.setController(cc);
	            incoming = cc.getTA();
	            
	            // Show the scene containing the root layout.
	           
	            Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TabPane rootLayout;
						try {
							rootLayout = (TabPane) loader.load();
							Scene scene = new Scene(rootLayout);
							st.setScene(scene);
							cc.initalizeUserList();
				            test.setCC(cc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
	            	
	            });
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public String getUser() {
		return un;
	}
	
}
