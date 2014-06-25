package com.daq.sensors;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class AdcProc extends Sensor{

	String pinNo,unit,quantity;
	
	
	public AdcProc(String sensorName,String quantity, String unit, String pinNo, 
			FormulaContainer fc) {
		super(sensorName,fc);
		this.pinNo = pinNo;
		this.unit=unit;
		this.quantity=quantity;
	}
	

	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("protocol", "adc");
			json.put("pin", pinNo);
			json.put("sensor_code", sensorName);
			json.put("quantity", quantity);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


	public String getQuantity(){
		return quantity;
	}


}
