package com.alextrost.onscreenjoystickdemo;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button startTCPButton;
	private Button startBTButton;
	private Button closeButton;
	private Button dummyButton;
	private EditText ipInput;
	private String ipString;
	public static boolean dummyStart;
	public static Connector connection;
	private Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.activity_main);
		startTCPButton = (Button)findViewById(R.id.startButton);
		startBTButton = (Button)findViewById(R.id.startBT);
		closeButton = (Button)findViewById(R.id.closeConn);
		dummyButton = (Button)findViewById(R.id.noConn);
		ipInput = (EditText)findViewById(R.id.ipInput);
		i = new Intent(this,JoystickMain.class);
		startTCPButton.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				ipString = ipInput.getText().toString();
				Toast.makeText(getApplicationContext(), "Ip received: "+ipString, Toast.LENGTH_SHORT).show();
				connection = new TcpConnector(ipString);
				String conn = "";
				try{
					conn = "Connections succesfull!";
					conn += " "+connection.createConnection();
				} catch(Exception e){
					e.printStackTrace();
					conn = "Connection refused! "+e.getLocalizedMessage();
				}
				dummyStart = false;
				Toast.makeText(getApplicationContext(), conn, Toast.LENGTH_LONG).show();
				startActivity(i);
			}
		});
		startBTButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ipString = ipInput.getText().toString();
				Toast.makeText(getApplicationContext(), "BT address received: "+ipString, Toast.LENGTH_SHORT).show();
				connection = new BTConnector(ipString,MainActivity.this);
				String conn = "";
				try{
					conn = "Connections succesfull!";
					conn += " "+connection.createConnection();
				} catch(Exception e){
					e.printStackTrace();
					conn = "Connection refused! "+e.getLocalizedMessage();
				}
				dummyStart = false;
				Toast.makeText(getApplicationContext(), conn, Toast.LENGTH_LONG).show();
				startActivity(i);
			}
		});
		dummyButton.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				ipString = ipInput.getText().toString();
				Toast.makeText(getApplicationContext(), "Ip received: "+ipString, Toast.LENGTH_SHORT).show();
				connection = new TcpConnector(ipString);
				String conn = "Starting Offline connection";
				Toast.makeText(getApplicationContext(), conn, Toast.LENGTH_LONG).show();
				dummyStart = true;
				startActivity(i);
			}
		});
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String toastText = "";
				if(connection != null){
					try {
						connection.closeConnection();
						toastText = "Connection Closed";
						finish();
					} catch (IOException e) {
						toastText = "Error closing connection";
					}
				}
				else{
					toastText = "Connection not even started!";
				}
				Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
