
package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ClientObserver extends PrintWriter implements Observer {
	public int universalPublic = 3;
	public int personalPublic =(int) (Math.random() * 100 + 1);
	private int personalPrivate = (int) (Math.random() * 100 + 1);
	public ClientObserver(OutputStream out) {
		super(out);
	}
	@Override
	public void update(Observable o, Object arg) {
		Scanner scan = new Scanner((String) arg);
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}

}
