package MyThread;

import java.net.Socket;

public class MakePDF implements Runnable{
	Socket client;
	public MakePDF(Socket client) {
		this.client = client;
	}
	public void run () {
		System.out.println("here");
	}
}
