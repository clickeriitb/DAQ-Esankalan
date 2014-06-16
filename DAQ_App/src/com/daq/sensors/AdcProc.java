package com.daq.sensors;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class AdcProc extends Sensor{

	String pinNo;
	float icRangeFrom,icRangeTo,inputRangeFrom,inputRangeTo;
	
	
	
	public AdcProc(String sensorName, String pinNo, 
			float icRangeFrom, float icRangeTo, FormulaContainer fc, float inputRangeFrom,
			float inputRangeTo) {
		super(sensorName,fc);
		this.pinNo = pinNo;
		this.icRangeFrom = icRangeFrom;
		this.icRangeTo = icRangeTo;
		this.inputRangeFrom = inputRangeFrom;
		this.inputRangeTo = inputRangeTo;
	}
	

	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("protocol", "adc");
			json.put("pin", pinNo);
			json.put("sensor_code", sensorName);
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
