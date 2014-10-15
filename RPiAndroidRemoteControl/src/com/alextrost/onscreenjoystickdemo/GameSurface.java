package com.alextrost.onscreenjoystickdemo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

	private Context _context;
	private GameThread _thread;
	private GameControls _controls;
	private Connector connection;
	private GameJoystick _joystick;
	private Map<Integer, Double> converter;
	private DecimalFormat df = new DecimalFormat("###.##");
	private double lastLeft;
	private double lastRight;
	private final int totalPower = 200;
	private final int minPower = 30;
	public GameSurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		_context = context;
		this.connection = MainActivity.connection;
		init();
	}

	private void init(){
		//initialize our screen holder
		SurfaceHolder holder = getHolder();
		holder.addCallback( this);
		converter = new HashMap<Integer, Double>();
		initializeMap();
		//initialize our game engine

		//initialize our Thread class. A call will be made to start it later
		_thread = new GameThread(holder, _context, new Handler(),this);
		setFocusable(true);


		_joystick = new GameJoystick(getContext().getResources());
		//contols
		_controls = new GameControls();
		setOnTouchListener(_controls);
	}


	private void initializeMap(){
		converter.put(0, 1.0); //0 - 10
		converter.put(10, 0.9); //10 - 20
		converter.put(20, 0.9); //20 - 30
		converter.put(30, 0.8); //30 - 40
		converter.put(40, 0.8); //40 - 50
		converter.put(50, 0.7); //50 - 60
		converter.put(60, 0.6); //60 - 70
		converter.put(70, 0.5); //70 - 80
		converter.put(80, 0.5); //80 - 90
		converter.put(90, 0.5); //90 - 100
		converter.put(100, 0.5); //100 - 110
		converter.put(110, 0.6); //110 - 120
		converter.put(120, 0.7); //120 - 130
		converter.put(130, 0.8); //130 - 140
		converter.put(140, 0.8); //140 - 150
		converter.put(150, 0.9); //150 - 160
		converter.put(160, 0.9); //160 - 170
		converter.put(170, 1.0); //170 - 180
	}
	private double findRation(double angle) {
		angle = Math.abs(angle);
		int angleDecimal = ((int) (angle / 10)) * 10;
		while(converter == null);
		double ratio = converter.get(angleDecimal);
		return ratio;
	}


	public void doDraw(Canvas canvas){

		//update the pointer
		_controls.update(null);
		
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setTextSize(50);
		
		float delta_x = _controls._touchingPoint.x - _controls.initx;
		float delta_y = _controls._touchingPoint.y - _controls.inity;
		double angle = Math.toDegrees(Math.atan2(delta_y, delta_x));
		double distance = Math.sqrt(Math.pow(delta_x,2)+Math.pow(delta_y, 2)); // a^2 + b^2 = c^2
				
		boolean forward = !(angle > 0 && angle < 180);

		double rightEngine;
		double leftEngine;
		double ratio = findRation(angle);
		if(Math.abs(angle) < 90){
			//TURNING RIGHT
			leftEngine = Math.round(totalPower * ratio);
			rightEngine = Math.round(totalPower * (1 - ratio));
		}
		else{
			//TURNING LEFT
			leftEngine = Math.round(totalPower * (1 - ratio));
			rightEngine = Math.round(totalPower * ratio);
		}
		distance = Math.min(300, distance);
		double distancePercetange = (distance / 300);
		leftEngine = leftEngine * distancePercetange + minPower;
		rightEngine = rightEngine * distancePercetange + minPower;
		leftEngine = Math.min(100,leftEngine);
		rightEngine = Math.min(100,rightEngine);
		leftEngine = Math.max(leftEngine, 30);
		rightEngine = Math.max(rightEngine, 30);
		if(distance < 10){
			//Stopping!
			leftEngine = 0;
			rightEngine = 0;
		}
		if(canvas != null){
			canvas.drawText("Direction: "+(forward ? "Forward!" : "Back!"), 250, 150, p);
			canvas.drawText("Left Engine: "+df.format(leftEngine)+". Right Engine: "+df.format(rightEngine)+". Total usage: "+(df.format(leftEngine+rightEngine)), 250, 250, p);
			canvas.drawText("Power: "+df.format(distancePercetange)+"%", 750, 150, p);
		}
		
		//           |
		//           |			
		//           |
		//-90 - -180 |   0 - (-90)
		//           |
		//___________|___________
		//           |
		//           |
		//+90 - +180 |   0 - 90
		//           |
		//           |
		
		String sendText = "";
		String lastSendText = "";
		if((Math.abs(lastLeft - leftEngine) > 9) || (Math.abs(lastRight - rightEngine) > 9)){
			//There was a change, send it to the engine!
			sendText = "Sending data to car";
			lastLeft = leftEngine;
			lastRight = rightEngine;
			String dataToSend = (forward ? "f" : "b")+","+((int)lastLeft)+","+((int)lastRight);
			if(!MainActivity.dummyStart){
				try {
					connection.sendData(dataToSend);
					sendText = "Data Sent!";
				} catch (Exception e) {
					sendText = "Error sending text!";
				}
			}
			else{
				sendText = "Dummy start!";
			}
		}
		else{
			sendText = "Not sending data";
		}
		lastSendText = "Left: "+((int)lastLeft)+". Right: "+((int)lastRight);
		Paint p2 = new Paint();
		p2.setColor(Color.RED);
		p2.setTextSize(50);
		if(canvas != null){
			canvas.drawText(lastSendText, 250, 400, p2);
			canvas.drawText(sendText, 250, 500, p2);
		}
		//draw the pointer
		//canvas.drawBitmap(_pointer, _controls._pointerPosition.x, _controls._pointerPosition.y, null);

		if(canvas != null){
			//draw the joystick background
			canvas.drawBitmap(_joystick.get_joystickBg(), 1150,450, null);
			//draw the dragable joystick
			canvas.drawBitmap(_joystick.get_joystick(),_controls._touchingPoint.x - 26, _controls._touchingPoint.y - 26, null);
		}
	}




	//these methods are overridden from the SurfaceView super class. They are automatically called 
	//when a SurfaceView is created, resumed or suspended.
	@Override 
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}
	private boolean retry;
	@Override 
	public void surfaceDestroyed(SurfaceHolder arg0) {
		retry = true;
		//code to end gameloop
		_thread.state = GameThread.STOPED;
		try {
			if(connection != null){
				connection.closeConnection();
				Log.d("JAVI LOG","Connection Closed");
			}
		} catch (IOException e1) {
			Log.d("JAVI LOG","Error closing connection");
		}
		while (retry) {
			try {
				//code to kill Thread
				_thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}

	}

	@Override 
	public void surfaceCreated(SurfaceHolder arg0) {
		if(_thread.state==GameThread.PAUSED){
			//When game is opened again in the Android OS
			_thread = new GameThread(getHolder(), _context, new Handler(),this);
			_thread.start();
		}else{
			//creating the game Thread for the first time
			_thread.start();
		}
	}

	public void Update() {
		// TODO Auto-generated method stub

	}

}
