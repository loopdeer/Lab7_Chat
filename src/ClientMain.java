

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Scanner;

public class ClientMain {
	
	PrintWriter writer;
	BufferedReader reader;
	
	public static void main(String[] args)
	{
		boolean noInterface = true;
		Scanner scan = new Scanner(System.in);
		ClientMain client = new ClientMain();
		try {
			client.setupClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(noInterface)
		{
			client.sendMessage(scan);
		}
	}
	
	private void setupClient() throws Exception
	{		
		Socket sock = new Socket("localhost", 7458);
		writer = new PrintWriter(sock.getOutputStream());
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		System.out.println("Established Connection");
		Thread readThread = new Thread(new IncomeHandler());
		readThread.start();
	}
	
	private void sendMessage(Scanner scan)
	{
		System.out.println("Type in a message: ");
		String message = scan.nextLine();
		writer.println(message);
		writer.flush();
	}
	
	class IncomeHandler implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message = "";
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
