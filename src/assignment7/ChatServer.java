package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

public class ChatServer extends Observable {
	//public int serverPublic = 7;
	//TODO make sure this can not be a userName
	public static final String SERVERNAME = "*SERVER*";
	private int serverPrivate = 17;
	private ArrayList<String> users = new ArrayList<String>();
	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			String userName = getUserName(clientSocket);
			users.add(userName);
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket, userName));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection with " + userName);
		}
	}
	private String getUserName(Socket clientSocket) {
		// TODO Auto-generated method stub
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		String id = "";
		
		boolean finished = false;
		while(!finished)
		{
			try {
				id = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(id != null && !id.equals(""))
				finished = true;
		}
		return id;
	}
	
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private String userID;

		public ClientHandler(Socket clientSocket, String id) {
			userID = id;
			try {
				Socket sock = clientSocket;
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}
			catch(SocketException dc)
			{
				System.out.println(userID + " has disconnected.");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("server read "+ message + " from " + userID);
					if(!checkForCommand(message))
					{
						setChanged();
						notifyObservers(userID + ": " + message);
					}
				}
			}
			catch(SocketException dc)
			{
				System.out.println(userID + " has disconnected.");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private boolean checkForCommand(String m)
		{
			Scanner commandChecker = new Scanner(m);
			if(commandChecker.next().equals(ChatClient.COMMANDSTART))
			{
				String command = commandChecker.next();
				System.out.println("The server has read the following command: " + command + " from " + userID);
				
				switch(command)
				{
				case "startconv" : //stuff
					//TODO delete/inspect this later
					String specialMessage = SERVERNAME;
					/*
					while(commandChecker.hasNext())
					{
						special += commandChecker.next();
						special += " ";
					}
					System.out.println(userID + " wants to start a conversation with these person(s): " + special);*/
					//ArrayList<String> users = new ArrayList<String>();
					while(commandChecker.hasNext())
					{
						specialMessage = specialMessage + " " + commandChecker.next() + " notify New_Group_Message_Request \n";
					}
					setChanged();
					notifyObservers(specialMessage);
					
					break;
				default: System.out.println("invalid command");
				}
				return true;
			}
			
			return false;
		}
	}
}
