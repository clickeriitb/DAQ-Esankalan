package com.daq.sensors;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.idl.daq.I2C_ItemClass;

public class I2CProc extends Sensor{
	
	String unit,quantity,i2cAddress,sda,scl;
	List<I2C_ItemClass> configList,execList;
	
	public I2CProc(){
		super();
	}

	@Override
	public JSONObject getJSON() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try{
			json.put("protocol", "i2c");
			json.put("sensor_code", sensorName);
			json.put("quantity", quantity.toLowerCase(Locale.US));
			json.put("pin", sda+":"+scl);
			json.put("i2c_id", i2cAddress);
			String config="";
			for(I2C_ItemClass i : configList){
				config+=i.getInfo()+",";
			}
			config = "["+config.substring(0, config.length()-2)+"]";
			json.put("config_cmd", config);
			String exec="";
			for(I2C_ItemClass i : execList){
				exec+=i.getInfo()+",";
			}
			exec = "["+exec.substring(0, exec.length()-2)+"]";
			json.put("cmd", exec);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}

	@Override
	public String display() {
		// TODO Auto-generated method stub
		return sensorName+ " " + quantity + " " + unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getI2cAddress() {
		return i2cAddress;
	}

	public void setI2cAddress(String i2cAddress) {
		this.i2cAddress = i2cAddress;
	}

	public String getSda() {
		return sda;
	}

	public void setSda(String sda) {
		this.sda = sda;
	}

	public String getScl() {
		return scl;
	}

	public void setScl(String scl) {
		this.scl = scl;
	}

	public List<I2C_ItemClass> getConfigList() {
		return configList;
	}

	public void setConfigList(List<I2C_ItemClass> configList) {
		this.configList = configList;
	}

	public List<I2C_ItemClass> getExecList() {
		return execList;
	}

	public void setExecList(List<I2C_ItemClass> execList) {
		this.execList = execList;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	

}
