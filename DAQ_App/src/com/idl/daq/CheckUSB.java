package com.idl.daq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

public class CheckUSB extends Activity{
	
	private GlobalState gS;
	private UsbManager mUsbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		checkForUSB();
		Intent i = new Intent(getApplicationContext(),SelectProtocol.class);
		startActivity(i);
	}

	private void checkForUSB() {
		// TODO Auto-generated method stub
		gS = (GlobalState) getApplicationContext();
		mUsbManager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
		if (mUsbManager.getAccessoryList() != null){
			L.d("USB");
			gS.setUsb(true);
			gS.initiateUSB();
		} else{
			L.d("Wifi");
			//gS.setUsb(false);
		}
//		Intent i = new Intent(getApplicationContext(),USBInput.class);
//		startService(i);
		
	}
	
	

}
