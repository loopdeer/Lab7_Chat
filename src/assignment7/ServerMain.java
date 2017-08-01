package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import assignment7.ChatServer.ClientHandler;

public class ServerMain extends Observable {
	
	public static void main(String[] args)
	{
		try
		{
			new ServerMain().serverSetup();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void serverSetup() throws IOException
	{
		ServerSocket serverSock = new ServerSocket(7458);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection");
		}	
	}
	
	class ClientHandler implements Runnable
	{
		private BufferedReader reader;
		public ClientHandler(Socket sock)
		{
			Socket chatSocket = sock;
			
			try{
				reader = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message;
			try
			{
				while((message = reader.readLine()) != null)
				{
					System.out.println("Message recieved: " + message);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	

}
