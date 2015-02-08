package MyThread;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.SocketTimeoutException;

public class MakePDF implements Runnable{
	Socket client;
	int time = 0;
	public MakePDF(Socket client) {
		this.client = client;
	}
	
	
	public void run () {
		Reader reader = null;
		Writer writer = null;
		try {
			client.setSoTimeout(30000);
			reader = new InputStreamReader(client.getInputStream());
			writer = new OutputStreamWriter(client.getOutputStream());
			char chars[] = new char[1024];
			StringBuilder stringB = new StringBuilder();
			int len;
			while ((len = reader.read(chars)) != -1) {
				stringB.append(new String(chars,0, len));
				if (stringB.indexOf("\r\n") != -1) {
					break;
				}
			}
			String[] rev = stringB.toString().split("\r\n");
			//writer.write("STORED\r\n"); //执行成功
			//writer.flush();
			if (rev.length > 1) {
				try {
					String cmd = rev[1];
					if (cmd.indexOf("wkhtmltopdf2") != -1) {
						Process process = Runtime.getRuntime().exec(cmd);
						//process.waitFor();
						InputStream cmdInput = process.getInputStream();
						BufferedReader read = new BufferedReader(new InputStreamReader(cmdInput));
						String result = null;
						do {
							result = read.readLine();
							if (result != null) {
								System.out.println(result);
							}
						} while (result != null); 
						System.out.println("STORED");
						writer.write("STORED\r\n"); //执行成功
						writer.flush();
					} else {
						writer.write("ERROR\r\n"); //执行失败
						writer.flush();
					}
				} catch (Exception e) {
					writer.write("ERROR\r\n"); //执行失败
					writer.flush();
					e.printStackTrace();
				}
			}
			//System.out.println(stringB);
		} catch (SocketTimeoutException e) {
			System.out.println("Client["+client.getInetAddress()+"] 链接超时");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println(Thread.currentThread().getName().toString());
				reader.close();
				writer.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
