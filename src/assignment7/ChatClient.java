package assignment7;

import java.io.*;
import java.net.*;
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
	private int id = (int) (Math.random() * 1000 + 1);
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
		writer.println(scan.next());
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
					
						cc.getTA().appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		/*TabPane tp = new TabPane();
		Tab single = new Tab();
		incoming = new TextArea();
		incoming.setEditable(false);
		incoming.setWrapText(true);
		ScrollPane sp = new ScrollPane(incoming);
		outgoing = new TextField();
		Button but = new Button("Send");*/
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
        } catch (IOException e) {
            e.printStackTrace();
        }
		//setUpNetworking();
	}
}
