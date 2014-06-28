package com.daq.sensors;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class UartProc extends Sensor {

	String command,quantity, pin, pin1, pin2;
	float baudRate;
	int byteValue;

	public UartProc(String sensorName, String pin1, String pin2,String pin,
			FormulaContainer fc, float baudRate, String command,String quantity,int byteValue) {
		super(sensorName, fc);
		this.pin=pin;
		this.pin1=pin1;
		this.pin2=pin2;
		this.command = command;
		this.baudRate = baudRate;
		this.quantity = quantity;
		this.byteValue = byteValue;
	}

	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("protocol", "uart");
			json.put("pin", pin);
			json.put("sensor_code", sensorName);
			json.put("baudrate", baudRate);
			json.put("command", command);
			json.put("quantity", quantity.toLowerCase(Locale.US));
			json.put("byte", byteValue);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public String getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}

	@Override
	public String display() {
		// TODO Auto-generated method stub
		return null;
	}

}