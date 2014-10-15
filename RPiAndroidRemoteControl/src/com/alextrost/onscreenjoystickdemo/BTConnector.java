package com.alextrost.onscreenjoystickdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class BTConnector implements Connector{
	
	private BluetoothSocket mmSocket;
	private InputStream mmInStream;
	int bytes = 1024;
	byte[] buffer = new byte[bytes];
	private OutputStream mmOutStream;
	private BluetoothDevice RPi;
	private String btAddr;
	private BluetoothAdapter mBluetoothAdapter;
	private Activity activity;
	private final int REQUEST_ENABLE_BT = 1;
	private BTConnectThread BTCT;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-" +
			"00805f9b34fb");
	public BTConnector(String addr, Activity ac){
		this.btAddr = addr;
		this.activity = ac;
	}

	@Override
	public String createConnection() throws IOException {
		String ans;
		Log.d("JAVI LOG", "Starting Bluetooth");
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.d("JAVI LOG","No BT on this device");
		    ans = "No Bluetooth on this device";
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Log.d("JAVI LOG","BT is not enabled, starting intent");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    this.activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		Log.d("JAVI LOG","Getting list of BT devices");
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if(device.getAddress().equals(this.btAddr)) {
					Log.d("JAVI LOG","RPi address found!");
					RPi = device;
				}
			}
		}
		Log.d("JAVI LOG","Starting BT Connection Thread");
		BTCT = new BTConnectThread(RPi);
		BTCT.start();
		Log.d("JAVI LOG","BT Connection Thread started");
		ans = "Bluetooth initialized";
		sendData(Client.INIT_HEY);
		ans = getData();
		Log.d("JAVI LOG","Connection to RPi initialized");
		return ans;
	}

	@Override
	public String sendData(String data) throws IOException {
		Log.d("JAVI LOG","BT Sending data");
		try {
			mmOutStream.write(bytes);
			Log.d("JAVI LOG","BT Data sent");
			return "Sent!";
			} catch (IOException e) {
				Log.d("JAVI LOG","BT Data send error: "+e.getMessage());
				return "ERROR in sending";
			}
	}
	public String getData() throws IOException {
		Log.d("JAVI LOG","BT getting data");
		try {
			int read = mmInStream.read(buffer, bytes, bytes);
			Log.d("JAVI LOG","BT got "+read+" bytes");
			if(read > 0){
				String ans = new String(buffer, "UTF-8"); 
				buffer = new byte[bytes];
				Log.d("JAVI LOG","BT read data successfully");
				return ans;
			}
			else{
				Log.d("JAVI LOG","BT no data to read");
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			Log.d("JAVI LOG","BT error encoding data");
			return "ERROR Encoding";
		}
		catch (IOException e){
			Log.d("JAVI LOG","Error reading from socket "+e.getMessage());
			return "ERROR reading";
		}
	}

	@Override
	public String closeConnection() throws IOException {
		Log.d("JAVI LOG","BT Clossing connection");
		sendData(Client.KTHXBYE);
		this.mmInStream.close();
		this.mmOutStream.close();
		this.mmSocket.close();
		Log.d("JAVI LOG","BT Connection closed");
		return "Closed";
	}
	
	//Inner class - BT Connect Thread
	private class BTConnectThread extends Thread{
		private final BluetoothSocket mmSocket;
		public BTConnectThread(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			try {
				Log.d("JAVI LOG","BT Thread creating socket");
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) { 
				Log.d("JAVI LOG","BT Thread error creating socket "+e.getMessage());
			}
			mmSocket = tmp;
		}
		public void run() {
			Log.d("JAVI LOG","BT Thread starting run function");
			mBluetoothAdapter.cancelDiscovery();
			try {
				mmSocket.connect();
			} catch (IOException connectException) {
			try {
				mmSocket.close();
			} catch (IOException closeException) { }
				return;
			}
			
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			try {
				Log.d("JAVI LOG","BT Thread creating I/O Streams");
				tmpIn = mmSocket.getInputStream();
				tmpOut = mmSocket.getOutputStream();
			} catch (IOException e) { 
				Log.d("JAVI LOG","BT Thread error creating I/O Streams "+e.getMessage());
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}
	}
	
}
