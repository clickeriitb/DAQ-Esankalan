package com.daq.sensors;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.daq.formula.FormulaContainer;

public class UartProc extends Sensor {

	String command,quantity, pin, pin1, pin2, unit;
	int byteValue,baudRate;

	public UartProc(String sensorName, String pin1, String pin2,String pin,
			FormulaContainer fc, int baudRate, String command,String quantity,String unit,int byteValue) {
		super(sensorName, fc);
		this.pin=pin;
		this.pin1=pin1;
		this.pin2=pin2;
		this.command = command;
		this.baudRate = baudRate;
		this.quantity = quantity;
		this.byteValue = byteValue;
		this.unit = unit;
	}
	
	public UartProc(){
		super();
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

	//Getters and Setters
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPin1() {
		return pin1;
	}

	public void setPin1(String pin1) {
		this.pin1 = pin1;
	}

	public String getPin2() {
		return pin2;
	}

	public void setPin2(String pin2) {
		this.pin2 = pin2;
	}

	public float getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getByteValue() {
		return byteValue;
	}

	public void setByteValue(int byteValue) {
		this.byteValue = byteValue;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	

}