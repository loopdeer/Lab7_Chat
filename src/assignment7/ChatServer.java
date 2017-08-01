package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class ChatServer extends Observable {
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
			Socket sock = clientSocket;
			userID = id;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("server read "+ message + " from " + userID);
					setChanged();
					notifyObservers(userID + ": " + message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
