package com.alextrost.onscreenjoystickdemo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class TcpConnector implements Connector{
	private String ipString;
	private Socket clientSocket;
	private int portNumber;
	public TcpConnector(String ip){
		this.ipString = ip;
		this.portNumber = 1337;
	}
	
	public String createConnection() throws IOException{
		Log.d("JAVI LOG", "Starting tcp connection");
		InetAddress serverAddr = InetAddress.getByName(ipString);
		Log.d("JAVI LOG", "IP found: "+serverAddr);
		Log.d("JAVI LOG", "Trying to open socket with ip: "+serverAddr+" and port: "+portNumber);
		clientSocket = new Socket(serverAddr, portNumber);
		Log.d("JAVI LOG", "Socket created");
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		Log.d("JAVI LOG", "Bufferes warmed up");
		Log.d("JAVI LOG", "Sent: "+Client.CUSTOM_MOVE);
		outToServer.writeBytes(Client.INIT_HEY);
		Log.d("JAVI LOG", "Initiallization protocol engaged");
		String ans = inFromServer.readLine();
		Log.d("JAVI LOG", "Server responded. All good.");
		return ans;
	}
	
	public String sendData(String data) throws IOException{
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(Client.CUSTOM_MOVE+","+data);
		String ans = inFromServer.readLine();
		return ans;
	}
	
	public String closeConnection() throws IOException{
		if(clientSocket != null){
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			outToServer.writeBytes(Client.KTHXBYE);	
			clientSocket.close();
		}
		return "All closed";
	}
}
