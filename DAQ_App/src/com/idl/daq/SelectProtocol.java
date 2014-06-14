package com.idl.daq;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class SelectProtocol extends Activity implements OnClickListener{

	private Spinner procSpinner;
	private Button procSubmit,graph;
	private UsbManager mUsbManager;
	
	Intent i;
	Boolean ifUsb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol);
		
		
		procSpinner = (Spinner) findViewById(R.id.protocol_spinner);
		procSubmit = (Button) findViewById(R.id.proc_button);
		graph = (Button) findViewById(R.id.graph);

		
		procSubmit.setOnClickListener(this);
		
		
//		ifUsb = getIntent().getExtras().getBoolean("connection");
				
	

	
	graph.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent i = new Intent(getApplicationContext(),SensorListActivity.class);
			startActivity(i);
		}
	});
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String proc = String.valueOf(procSpinner.getSelectedItem());
		JSONObject json = new JSONObject();
		if(proc.equals("ADC")){
			try {
				i = new Intent(getApplicationContext(),AdcProcActivity.class);
				/*json.put("protocol1", "adc");
				json.put("sensorName", "LM35");
				json.put("pin", "P9_40");
				json.put("rate", 500);
				socket.emit("request for data", json);*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e("adc err", e.toString());
			}
		}
			else if(proc.equals("I2C")){
			try {
				json.put("protocol1", "i2c");
				json.put("sensorName", "MPU");
				json.put("pin","P9");
				json.put("addr", "68");	
				json.put("rate", 100);
				json.put("registers", "41,42");
				//socket.emit("request for data", json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e("i2c err", e.toString());
			}
		}else if(proc.equals("UART")){
			try {
				i = new Intent(getApplicationContext(),UartProcActivity.class);
				/*json.put("protocol1", "uart");
				json.put("sensorName", "GY");
				json.put("pin", "P9_40");
				json.put("rate", 100);
				json.put("byte1", "8");
				json.put("command", "31");
				json.put("baudrate", "9600");*/
				//socket.emit("request for data", json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e("uart err", e.toString());
			}
		}else if(proc.equals("SPI")){
			
		}
//		try {
//			json.put("objId", id);
//			json.put("message", message);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		i.putExtra("connection",ifUsb );
		startActivity(i);
		finish();
	}
	
}