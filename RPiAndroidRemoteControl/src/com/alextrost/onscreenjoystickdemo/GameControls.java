package com.alextrost.onscreenjoystickdemo;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameControls implements OnTouchListener{

	public float initx = 1390;
	public float inity = 700;
	public float maxMovement = 300;
	public Point _touchingPoint = new Point(1390,700);
	
	private Boolean _dragging = false;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		update(event);
		return true;
	}

	private MotionEvent lastEvent;
	public void update(MotionEvent event){

		if (event == null && lastEvent == null)
		{
			return;
		}else if(event == null && lastEvent != null){
			event = lastEvent;
		}else{
			lastEvent = event;
		}
		//drag drop 
		if ( event.getAction() == MotionEvent.ACTION_DOWN ){
			_dragging = true;
		}else if ( event.getAction() == MotionEvent.ACTION_UP){
			_dragging = false;
		}
		

		if ( _dragging ){
			// get the pos
			_touchingPoint.x = (int)event.getX();
			_touchingPoint.y = (int)event.getY();

			// bound to a box
			if( _touchingPoint.x < initx-maxMovement){
				_touchingPoint.x = (int) (initx-maxMovement);
			}
			if ( _touchingPoint.x > initx+maxMovement){
				_touchingPoint.x = (int) (initx+maxMovement);
			}
			if (_touchingPoint.y < inity-maxMovement){
				_touchingPoint.y = (int) (inity-maxMovement);
			}
			if ( _touchingPoint.y > inity+maxMovement){
				_touchingPoint.y = (int) (inity+maxMovement);
			}
			
		}else if (!_dragging)
		{
			// Snap back to center when the joystick is released
			_touchingPoint.x = (int) initx;
			_touchingPoint.y = (int) inity;
			//shaft.alpha = 0;
		}
	}
}
