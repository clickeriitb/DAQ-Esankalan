package com.daq.sensors;

import org.json.JSONException;
import org.json.JSONObject;

public class UartProc extends Sensor{

	String pin1,pin2,command;
	float baudRate;
	
	
	
	public UartProc(String sensorName, String command, 
			String pin1, String pin2, String formula, float baudRate) {
		super(sensorName,formula);
		this.pin1 = pin1;
		this.pin2 = pin2;
		this.command = command;
		this.baudRate = baudRate;
	}
	
	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("protocol1", "uart");
			json.put("pin1", pin1);
			json.put("pin2", pin2);
			json.put("sensorName", sensorName);
			json.put("baudRate", baudRate);
			json.put("command", command);
//			json.put("inputRangeFrom", inputRangeFrom);
//			json.put("inputRangeTo", inputRangeTo);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


}