import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;


public class lf_website {

	public static void main(String[] args) throws InterruptedException {
		lf_website manager =new lf_website();
		// 设置守护线程
		manager.setDeamon();
		manager.setPdf();
	}
	
	/**
	 * 处理生成pdf的线程
	 * 
	 * @return void
	 */
	public void setPdf() {
		Runnable userPdf = new Runnable() {
			public void run() {
				ServerSocket server;
				try {
					server = new ServerSocket(9990);
					while (true) {
						Socket client = server.accept();
						System.out.println("Accepted a pdf request, it is from " + client.getInetAddress()+ " : " + client.getPort() + "." );
						new Thread(new MyThread.MakePDF(client)).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(userPdf).start();
	}
	
	/**
	 *  设置守护线程
	 *  
	 *  @return void
	 */
	public void setDeamon() {
		Runnable deamon = new Runnable() {
			public void run() {
				ServerSocket server;
				try {
					server = new ServerSocket(9991);
					while (true) {
						Socket client = server.accept();
						System.out.println("Accepted a request, it is from " + client.getInetAddress()+ " : " + client.getPort() + "." );
						Thread myDeamon = new Thread(new MyThread.Telnet(client));
						myDeamon.setDaemon(true);
						myDeamon.start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(deamon).start();
	}
}
