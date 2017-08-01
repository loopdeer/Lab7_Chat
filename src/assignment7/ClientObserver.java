
package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ClientObserver extends PrintWriter implements Observer {
	public ClientObserver(OutputStream out) {
		super(out);
	}
	@Override
	public void update(Observable o, Object arg) {
		Scanner scan = new Scanner((String) arg);
		String userID = scan.next();
		userID = userID.substring(0, userID.length() - 1);
		if(userID.equals("test"))
		{
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
		}
	}

}
