package com.alextrost.onscreenjoystickdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class JoystickMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set to full screen 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameSurface(this));
    }
}