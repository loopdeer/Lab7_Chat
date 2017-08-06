package assignment7;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




public class ChatClient extends Application {
	private static TextArea incoming;
	private TextField outgoing;
	private BufferedReader reader;
	private static PrintWriter writer;
	private static ClientMainController cc;
	private String userID;
	public static final String COMMANDSTART = "*/";
	
	//private String groupIdentity;
	public int universalPublic = 3;
	public int personalPublic =(int) (Math.random() * 100 + 1);
	private int personalPrivate = (int) (Math.random() * 100 + 1);
	
	private TabPane rootLayout;
	
	public void run() throws Exception {
		setUpNetworking();
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		
		//delete this later/replace with UI
		Scanner scan = new Scanner(System.in);
		userID = scan.next();
		writer.println(COMMANDSTART + " initializeID " + userID);
		writer.flush();
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	

	public static void main(String[] args) {
		ChatClient test = new ChatClient();
		try {
			test.run();
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
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientMain.class.getResource("ClientMain.fxml"));
            cc = new ClientMainController(writer);
            incoming = cc.getTA();
            loader.setController(cc);
            rootLayout = (TabPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            cc.initalizeUserList();
        } catch (IOException e) {
            e.printStackTrace();
        }
		//setUpNetworking();
	}
}
