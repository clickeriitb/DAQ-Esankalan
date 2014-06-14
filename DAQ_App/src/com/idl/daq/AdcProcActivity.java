package com.idl.daq;

import com.daq.sensors.AdcProc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class AdcProcActivity extends Activity implements OnClickListener{
	
	EditText sensorName,pinNo,icRangeFrom,icRangeTo,formula,inputRangeFrom,inputRangeTo;
	Button submit;
	AdcProc adcSensor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1_adc);
		
		defineAttributes();
		submit.setOnClickListener(this);
	}


	private void defineAttributes() {
		// TODO Auto-generated method stub
		sensorName = (EditText) findViewById(R.id.sensor_name);
		pinNo = (EditText) findViewById(R.id.pin_no);
		icRangeFrom= (EditText) findViewById(R.id.ic_range1);
		icRangeTo = (EditText) findViewById(R.id.ic_range2);
		formula = (EditText) findViewById(R.id.formula);
		inputRangeFrom= (EditText) findViewById(R.id.input_range1);
		inputRangeTo = (EditText) findViewById(R.id.input_range2);
		submit = (Button) findViewById(R.id.submit);
	}
	
	private void fillForm() {
		// TODO Auto-generated method stub
		String sensor = sensorName.getText().toString();
		String pinNum = pinNo.getText().toString();
		String formulaString = formula.getText().toString();
		float icFrom = Float.parseFloat(icRangeFrom.getText().toString());
		float icTo = Float.parseFloat(icRangeTo.getText().toString());
		float inputFrom = Float.parseFloat(inputRangeFrom.getText().toString());
		float inputTo = Float.parseFloat(inputRangeFrom.getText().toString());
		
		
		adcSensor = new AdcProc(sensor,pinNum,icFrom,icTo,formulaString,inputFrom,inputTo);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		fillForm();
		GlobalState gS = (GlobalState) getApplicationContext();
		boolean isUSB = gS.isUsb();
		gS.addAdcProc(adcSensor);
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
