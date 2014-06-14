package com.idl.daq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity{

	private static int SPLASH_TIME_OUT = 1500;
	private Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				i = new Intent(getApplicationContext(),CheckUSB.class); 
				startActivity(i);
				finish();
			}
			
		}, SPLASH_TIME_OUT);
	}

	
}
