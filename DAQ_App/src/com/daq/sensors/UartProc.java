package com.daq.sensors;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class UartProc extends Sensor {

	String pin1, pin2, command,quantity;
	float baudRate;
	int byteValue;

	public UartProc(String sensorName, String pin1, String pin2,
			FormulaContainer fc, float baudRate, String command,String quantity,int byteValue) {
		super(sensorName, fc);
		this.pin1 = pin1;
		this.pin2 = pin2;
		this.command = command;
		this.baudRate = baudRate;
		this.quantity = quantity;
		this.byteValue = byteValue;
	}

	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("protocol", "uart");
			//json.put("pin1", pin1);
			//json.put("pin2", pin2);
			json.put("pin", "UART1");
			json.put("sensor_code", sensorName);
			json.put("baudRate", baudRate);
			json.put("command", command);
			json.put("quantity", quantity.toLowerCase(Locale.US));
			json.put("byte", byteValue);
			// json.put("inputRangeFrom", inputRangeFrom);
			// json.put("inputRangeTo", inputRangeTo);

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

}