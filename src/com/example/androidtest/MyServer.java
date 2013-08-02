package com.example.androidtest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 *@author Andrew.Lee
 *@create 2011-6-1 ����04:45:19
 *@version 1.0
 *@see
 */
public class MyServer extends Handler {
	private static final String TAG = MyServer.class.getName();
	static ServerSocket aServerSocket = null; // Server Socet.
	DataInputStream aDataInput = null; // Server input Stream that to
	// receive msg from client.
	DataOutputStream aDataOutput = null; // Server output Stream that to
	private DatagramSocket mDgSocket;
	private Context mContext;
	static ArrayList list = new ArrayList();

	
	public MyServer(Context context, Looper looper) {
		super(looper);
		mContext = context;

		
	}
	
    public void handleMessage(Message msg) {
    	switch (msg.what) {
		case 0:
			start();
			break;

		default:
			break;
		}
    }
	
	private void start() {
		Log.d(TAG, "in start");
		//startListenPair();
		//startListenUDPBroadcast();
		startSSLClient();
	}

	private void startSSLClient() {
		new Client().start();
	}

	private void startListenUDPBroadcast() {
		try {
			mDgSocket = new DatagramSocket(9101);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		byte[] buffer = new byte[256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while(true) {
			try {
				mDgSocket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			handleRequestPacket(packet);
		}
	}
	
	  private void handleRequestPacket(DatagramPacket packet) {
		  String strPacket = new String(packet.getData(), 0, packet.getLength());
		  Log.d(TAG, "in startListenUDPBroadcast, strPacket=" + strPacket);
		    String tokens[] = strPacket.trim().split("\\s+");

		    if (tokens.length != 3) {
		      Log.w(TAG, "Malformed response: expected 3 tokens, got "
		          + tokens.length);
		      return;
		    }
		    String requestType = tokens[0];
		    String deviceName = tokens[1];
		    int port = Integer.parseInt(tokens[2]);
		    InetAddress addr = packet.getAddress();

		    packet = makeResponsePacket(addr, port);
		    try {
				mDgSocket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
	  }
	  
	  private static final String DESIRED_SERVICE = "_anymote._tcp";
	  private static final String SERVICE_NAME = "sowell-STB";
	  private DatagramPacket makeResponsePacket(InetAddress addr, int responsePort) {
		    String message = DESIRED_SERVICE + " " + SERVICE_NAME
		        + " " + responsePort + "\n";
		    byte[] buf = message.getBytes();
		    DatagramPacket packet = new DatagramPacket(buf, buf.length,
		    		addr, responsePort);
		    return packet;
	}

	private void startListenPair() {
		try {
			aServerSocket = new ServerSocket(9552); // listen 8888 port.
			System.out.println("already listen 9552 port.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int num = 0;
		while (num < 10) {
			Socket aSessionSoket = null;
			try {
				aSessionSoket = aServerSocket.accept();
				Log.d(TAG, "in start, accept: ipaddr=" + aSessionSoket.getInetAddress().getHostAddress());
				MyThread thread = new MyThread(aSessionSoket);
				thread.start();
				num = list.size();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	class MyThread extends Thread {
		Socket aSessionSoket = null;

		public MyThread(Socket socket) {
			aSessionSoket = socket;
		}

		public void run() {
			try {
				aDataInput = new DataInputStream(aSessionSoket.getInputStream());
				aDataOutput = new DataOutputStream(aSessionSoket
						.getOutputStream());
				list.add(aDataOutput);
				while (true) {
					Log.d(TAG, "in MyThread, read=" + aSessionSoket.getInputStream().read());
//					String msg = aDataInput.readUTF(); // read msg.
//					if (!msg.equals("connect...")) {
//						System.out.println("ip: "
//								+ aSessionSoket.getInetAddress());// ip.
//						System.out.println("receive msg: " + msg);
//						for (int i = 0; i < list.size(); i++) {
//							DataOutputStream output = (DataOutputStream) list
//									.get(i);
//							output.writeUTF(msg + "----" + list.size());
//						}
//						if (msg.equals("end"))
//							break;
//					}
//					aDataOutput.writeUTF("");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					aDataInput.close();
					if (aDataOutput != null)
						aDataOutput.close();
					list.remove(aDataOutput);
					aSessionSoket.close();

				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}

		}
	}
	class Client {    
		   
	    private static final String DEFAULT_HOST                     = "192.168.1.156";    
	    private static final int     DEFAULT_PORT                     = 7777;    
	   
	    private static final String CLIENT_KEY_STORE_PASSWORD        = "123456";    
	    private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";    
	   
	    private SSLSocket            sslSocket;
		private KeyStoreManager mKeyStoreManager;    
	   
	    /** 
	      * 启动客户端程序 
	      * 
	      * @param args 
	      */   
	    public void start() {    
	         init();    
	         process();  
	     }    
	   
	   
	    public void process() {    
	        if (sslSocket == null) {    
	             System.out.println("ERROR");    
	            return;    
	         }    
	        try {    
	             InputStream input = sslSocket.getInputStream();    
	             OutputStream output = sslSocket.getOutputStream();    
	   
	             BufferedInputStream bis = new BufferedInputStream(input);    
	             BufferedOutputStream bos = new BufferedOutputStream(output);    
	   
	             bos.write("1234567890".getBytes());    
	             bos.flush();    
	   
	            byte[] buffer = new byte[20];    
	             bis.read(buffer);    
	             System.out.println(new String(buffer));    
	   
	             sslSocket.close();    
	         } catch (IOException e) {    
	             System.out.println(e);    
	         }    
	     }    
	   
	   
		public void init() {
			try {
				mKeyStoreManager = new KeyStoreManager();
				mKeyStoreManager.initialize(mContext);
				SSLContext ctx = SSLContext.getInstance("TLS");

//				KeyManagerFactory kmf = KeyManagerFactory
//						.getInstance("X509");
//				TrustManagerFactory tmf = TrustManagerFactory
//						.getInstance("X509");
//
//				KeyStore ks = KeyStore.getInstance("BKS");
//				KeyStore tks = KeyStore.getInstance("BKS");
//
//				ks.load(getFromAssets("kclient.keystore"), CLIENT_KEY_STORE_PASSWORD.toCharArray());
//				// tks.load(new FileInputStream("src/ssl/tclient.keystore"),
//				// CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());
//
//				kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
//				tmf.init(ks);

				//ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ctx.init(mKeyStoreManager.getKeyManagers(), mKeyStoreManager.getTrustManagers(), null);

				sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(
						DEFAULT_HOST, DEFAULT_PORT);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		// 从assets 文件夹中获取文件并读取数据
		public InputStream getFromAssets(String fileName) {
			InputStream in = null;
			try {
				in = mContext.getResources().getAssets().open(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return in;
		}
	   
	}  
	
}



