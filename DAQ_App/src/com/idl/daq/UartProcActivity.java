package com.idl.daq;

import com.daq.sensors.UartProc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UartProcActivity extends Activity implements OnClickListener{
	
	EditText sensorName,pinOne,pinTwo,formula,command,baudRate;
	Button submit;
	UartProc uartSensor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uart);
		
		defineAttributes();
		submit.setOnClickListener(this);
	}


	private void defineAttributes() {
		// TODO Auto-generated method stub
		sensorName = (EditText) findViewById(R.id.sensor_name);
		pinOne = (EditText) findViewById(R.id.pin1);
		pinTwo = (EditText) findViewById(R.id.pin2);
		formula = (EditText) findViewById(R.id.formula);
		command = (EditText) findViewById(R.id.command);
		baudRate = (EditText) findViewById(R.id.baud);
		
		submit = (Button) findViewById(R.id.submit);
	}
	
	private void fillForm() {
		// TODO Auto-generated method stub
		String sensor = sensorName.getText().toString();
		String comm = command.getText().toString();
		String pin1= pinOne.getText().toString();
		String pin2= pinTwo.getText().toString();
		String formulaString = formula.getText().toString();
		float baud = Float.parseFloat(baudRate.getText().toString());
		
		uartSensor = new UartProc(sensor, comm, pin1, pin2, formulaString, baud);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		fillForm();
		GlobalState gS = (GlobalState) getApplicationContext();
		boolean isUSB = gS.isUsb();
		gS.addUartProc(uartSensor);
		Intent i;
//		if(isUSB){
//			i = new Intent(getApplicationContext(),MainActivity.class);
//		} else {
//			i = new Intent(getApplicationContext(),SocketLoaderUi.class);
//		}
		i = new Intent(getApplicationContext(),SensorListActivity.class);
		startActivity(i);
		finish();
	}
	
}