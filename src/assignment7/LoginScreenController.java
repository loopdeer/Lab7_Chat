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
import javafx.scene.control.SplitPane;
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
	@FXML
	private Label ipLabel;
	@FXML
	private Label portLabel;
	@FXML
	private TextField ipField;
	@FXML
	private TextField portField;
	
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
		try {
			String username = userField.getText();
			String ip = ipField.getText();
			int port = Integer.parseInt(portField.getText());
			
			if (username.equals("")) {
				Stage stage = new Stage();
				FXMLLoader ldr = new FXMLLoader();
	            ldr.setLocation(ClientMain.class.getResource("Error.fxml"));
	            ErrorController ec = new ErrorController(stage);
	            ldr.setController(ec);
	            TitledPane tp = (TitledPane) ldr.load();
	            Scene error = new Scene(tp);
	            Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						stage.setScene(error);
						ec.getLabel().setText("Please enter a username!");
						stage.show();
					}
	            	
	            });
			}
			
			else if (ip.equals("")) {
				Stage stage = new Stage();
				FXMLLoader ldr = new FXMLLoader();
	            ldr.setLocation(ClientMain.class.getResource("Error.fxml"));
	            ErrorController ec = new ErrorController(stage);
	            ldr.setController(ec);
	            TitledPane tp = (TitledPane) ldr.load();
	            Scene error = new Scene(tp);
	            Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						stage.setScene(error);
						ec.getLabel().setText("Please enter an IP Address!");
						stage.show();
					}
	            	
	            });
			}
			
			else if (port >= 0 && port <= 1023) {
				Stage stage = new Stage();
				FXMLLoader ldr = new FXMLLoader();
	            ldr.setLocation(ClientMain.class.getResource("Error.fxml"));
	            ErrorController ec = new ErrorController(stage);
	            ldr.setController(ec);
	            TitledPane tp = (TitledPane) ldr.load();
	            Scene error = new Scene(tp);
	            Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						stage.setScene(error);
						ec.getLabel().setText("Please enter an unreserved port!");
						stage.show();
					}
	            	
	            });
			}
			else {
				un = username;
				ChatClient test = new ChatClient();
				try {
					writer = test.run(un, ip, port);
					if(writer != null)
					{
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
							SplitPane rootLayout;
							try {
								rootLayout = (SplitPane) loader.load();
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
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (NumberFormatException e) {
			Stage stage = new Stage();
			FXMLLoader ldr = new FXMLLoader();
            ldr.setLocation(ClientMain.class.getResource("Error.fxml"));
            ErrorController ec = new ErrorController(stage);
            ldr.setController(ec);
            TitledPane tp = (TitledPane) ldr.load();
            Scene error = new Scene(tp);
            Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					stage.setScene(error);
					ec.getLabel().setText("Please enter a port!");
					stage.show();
				}
            	
            });

	}
	}
	
	public String getUser() {
		return un;
	}
	
	public void userTaken()
	{
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Stage primaryStage = new Stage();
				FXMLLoader ldr = new FXMLLoader();
		        ldr.setLocation(LoginScreenController.class.getResource("Error.fxml"));
		        ldr.setController(new ErrorController(primaryStage));
				Scene login;
				try {
					login = new Scene(ldr.load());
					primaryStage.setScene(login);
			        primaryStage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
			}
			
		});
		{
	}
	}
}

