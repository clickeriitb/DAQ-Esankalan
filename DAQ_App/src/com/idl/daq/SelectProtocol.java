package com.idl.daq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectProtocol extends Activity implements OnClickListener{

	private ImageButton adc,uart,i2c;
	String proc;
	
	Intent i;
	GlobalState gS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_protocol);
		
		
		adc = (ImageButton) findViewById(R.id.adc);
		uart = (ImageButton) findViewById(R.id.uart);
		i2c = (ImageButton) findViewById(R.id.i2c);
		adc.setOnClickListener(this);
		uart.setOnClickListener(this);
		i2c.setOnClickListener(this);
		gS = (GlobalState) getApplicationContext();
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.adc)
		{
			proc="ADC";
		}
		else if(v.getId()==R.id.uart)
		{
			proc="UART";
		}
		else if(v.getId()==R.id.i2c)
		{
			proc="I2C";
		}
		i = new Intent(getApplicationContext(),SensorFormActivity.class);
		gS.setProtocol(proc);
		startActivity(i);
		finish();
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
	
}