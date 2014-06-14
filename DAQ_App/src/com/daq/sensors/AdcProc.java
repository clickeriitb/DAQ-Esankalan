package com.daq.sensors;

import org.json.JSONException;
import org.json.JSONObject;

public class AdcProc extends Sensor{

	String pinNo;
	float icRangeFrom,icRangeTo,inputRangeFrom,inputRangeTo;
	
	
	
	public AdcProc(String sensorName, String pinNo, 
			float icRangeFrom, float icRangeTo, String formula, float inputRangeFrom,
			float inputRangeTo) {
		super(sensorName,formula);
		this.pinNo = pinNo;
		this.icRangeFrom = icRangeFrom;
		this.icRangeTo = icRangeTo;
		this.inputRangeFrom = inputRangeFrom;
		this.inputRangeTo = inputRangeTo;
	}
	

	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("protocol1", "adc");
			json.put("pin", pinNo);
			json.put("sensorName", sensorName);
			json.put("icRangeFrom", icRangeFrom);
			json.put("icRangeTo", icRangeTo);
			json.put("inputRangeFrom", inputRangeFrom);
			json.put("inputRangeTo", inputRangeTo);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


	


}
