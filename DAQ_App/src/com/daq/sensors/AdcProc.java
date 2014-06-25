package com.daq.sensors;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class AdcProc extends Sensor{

	String pinNo,unit,quantity;
	float icRangeFrom,icRangeTo,inputRangeFrom,inputRangeTo;
	
	public AdcProc(String sensorName,String quantity, String unit, String pinNo, FormulaContainer fc) {
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
			json.put("quantity", quantity.toLowerCase(Locale.US));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return json;
	}


	public String getQuantity(){
		return quantity;
	}

	@Override
	public String display() {
		// TODO Auto-generated method stub
		return sensorName+ " " + quantity + " " + unit;
	}
}
