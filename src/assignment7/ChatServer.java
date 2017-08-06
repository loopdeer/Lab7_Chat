package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

public class ChatServer extends Observable {
	//public int serverPublic = 7;
	//TODO make sure this can not be a userName
	public static final String SERVERNAME = "*SERVER*";
	public static final String ALLNAME = "*ALL*";
	private int serverPrivate = 17;
	private ArrayList<String> onlineUsers = new ArrayList<String>();
	private HashMap<String, Integer> secrets = new HashMap<String, Integer>();
	public static void main(String[] args) {
		try {
			new ChatServer().setUpNetworking();
			LoginScreenController.users = new HashSet<String>();
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
			onlineUsers.add(userName);
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket, userName, writer));
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
			
			if(id != null && !id.equals("") && id.contains(ChatClient.COMMANDSTART + " initializeID "))
			{
				finished = true;
			}
				
		}
		return id.substring(16);
	}
	
	private int getInteger(Socket clientSocket)
	{
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
					
					if(id != null && !id.equals("") && id.contains(ChatClient.COMMANDSTART + " initializeInt "))
					{
						finished = true;
					}
						
				}
				return Integer.parseInt(id.substring(17));
	}
	
	private void disconnect(String id, ClientObserver o)
	{
		System.out.println(id + " has disconnected.");
		this.deleteObserver(o);
		System.out.println("# of observers: " + this.countObservers());
		
	}
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private String userID;
		private ClientObserver obv;

		public ClientHandler(Socket clientSocket, String id, ClientObserver ob) {
			userID = id;
			obv = ob;
			try {
				Socket sock = clientSocket;
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}
			catch(SocketException dc)
			{
				disconnect(userID, obv);
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
						notifyObservers(userID + " " + message);
					}
				}
			}
			catch(SocketException dc)
			{
				disconnect(userID, obv);
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
					String specialMessage = "";
					ArrayList<String> users = new ArrayList<String>();
					while(commandChecker.hasNext())
					{
						users.add(commandChecker.next());
					}
					
					users.add(userID);
					Collections.sort(users);
					String groupIdent = users.toString();
					groupIdent = groupIdent.replace("[", "");
					groupIdent = groupIdent.replaceAll(" ", "");
					groupIdent = groupIdent.replace("]", "");
					for(String u : users)
					{
						specialMessage = specialMessage + SERVERNAME + " " + u + " notifyNewGroup " + groupIdent + "\n";
					}
					
					setChanged();
					notifyObservers(specialMessage);
					
					break;
				case "getOnlineUsers" :
					Collections.sort(onlineUsers);
					specialMessage = onlineUsers.toString();
					specialMessage = specialMessage.replace("[", "");
					specialMessage = specialMessage.replaceAll(",", "");
					specialMessage = specialMessage.replace("]", "");
					specialMessage = SERVERNAME + " " + ALLNAME + " onlineUsers " + specialMessage; 
					
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
