package assignment7;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;




public class ChatClient extends Application {
	private static TextArea incoming;
	private TextField outgoing;
	private BufferedReader reader;
	private static PrintWriter writer;
	private static ClientMainController cc;
	private String userID;
	private static LoginScreenController lsc;
	//private boolean loggedIn = false;
	public static final String COMMANDSTART = "*/";
	
	//private String groupIdentity;
	public int universalPublic = 3;
	public int personalPublic =(int) (Math.random() * 100 + 1);
	private int personalPrivate = (int) (Math.random() * 100 + 1);
	
	private TabPane rootLayout;
	
	public PrintWriter run(String user) throws Exception {
		return setUpNetworking(user);
		
	}
	
	private boolean checkUserName(String m)
	{
		if(m == null)
			return false;
		Scanner commandChecker = new Scanner(m);
		if(commandChecker.hasNext())
		{
			String sender = commandChecker.next();
			if(sender.equals(ChatServer.SERVERNAME))
			{
				String command = commandChecker.next();
					if(command.equals("userTaken"))
					{
						lsc.userTaken();
						return false;
					}
					else if(command.equals("userFree"))
					{
						return true;
					}
			}
		}
		return false;
	}

	private PrintWriter setUpNetworking(String user) throws Exception {
		String message;
		
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		//delete this later/replace with UI
		/*FXMLLoader ldr = new FXMLLoader();
        ldr.setLocation(ClientMain.class.getResource("LoginScreen.fxml"));
        LoginScreenController lsc = new LoginScreenController();
        ldr.setController(lsc);
        TitledPane tp = (TitledPane) ldr.load();
        Scene login = new Scene(tp);
        Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Stage stage = new Stage();
				stage.setScene(login);
				stage.show();
			}
        	
        });*/
		//primaryStage.setScene(login);
		//Scanner scan = new Scanner(System.in);
		//userID = scan.next();
		userID = user;
        
		writer.println(COMMANDSTART + " initializeID " + userID);
		//System.out.println(writer);
		writer.flush();
		while(((message = reader.readLine()) == null));
		
		if(!checkUserName(message))
			return null;
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		System.out.println("networking established");
		
		return writer;
	}
	
	

	public static void main(String[] args) {
		//ChatClient test = new ChatClient();
		try {
			//test.run();
			launch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
						/*Scanner scan = new Scanner(message);
						String userID = scan.next();
						userID = userID.substring(0, userID.length() - 1);*/
						/*if(!checkSender(message))
							cc.getTA().appendText(message + "\n");*/
					checkSender(message);
						
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		private void checkSender(String m)
		{
			//messages are formatted as such: <sender> <recipients> <messages/command>
			//server messages/commands come in the format: <server designated specialID> <user> <command> ...
			Scanner commandChecker = new Scanner(m);
			if(commandChecker.hasNext())
			{
				String sender = commandChecker.next();
				String recipient = commandChecker.next();
				if(sender.equals(ChatServer.SERVERNAME))
				{
					if(recipient.equals(userID) || recipient.equals(ChatServer.ALLNAME))
					{
						switch(commandChecker.next())
						{
						case "notifyNewGroup" : 
							String groupIdentity = commandChecker.nextLine();
							groupIdentity = groupIdentity.replaceAll(" ", "");
							cc.getTA().appendText("New conversation group request for: " + groupIdentity + "\n");
							cc.setGroup(groupIdentity);
							break;
						case "onlineUsers" :
							String ul = commandChecker.nextLine();
							cc.updateOnlineTab(ul);
							break;
						default : cc.getTA().appendText("The server wanted to tell you something...but it screwed up." + "\n");
						}
					}
					return;
				}
				else
				{
					cc.handleMessage(sender, recipient, commandChecker.nextLine());
				}
			}
			
		}
		
		
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {
			FXMLLoader ldr = new FXMLLoader();
	        ldr.setLocation(LoginScreenController.class.getResource("LoginScreen.fxml"));
	        lsc = new LoginScreenController(primaryStage, writer);
	        ldr.setController(lsc);
	        TitledPane tp = (TitledPane) ldr.load();
	        Scene login = new Scene(tp);
	        primaryStage.setScene(login);
	        primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		//setUpNetworking();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
	}
	
	public void setCC(ClientMainController c)
	{
		cc = c;
	}
}
