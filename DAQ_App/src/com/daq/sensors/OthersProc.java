package com.daq.sensors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class OthersProc extends Sensor {

	File f = null;
	String quantity;
	
	@Override
	public JSONObject getJSON() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		try {
			json.put("protocol", "others");
			json.put("sensor_code", sensorName);
			json.put("quantity", quantity);
			String code = getFileCode(f);
			json.put("code", code);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	

	private String getFileCode(File f) {
		// TODO Auto-generated method stub
		StringBuilder text = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return text.toString();
	}



	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



	@Override
	public String display() {
		// TODO Auto-generated method stub
		return sensorName;
	}

	@Override
	public String getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}

	public File getF() {
		return f;
	}

	public void setF(File f) {
		this.f = f;
	}
	
	

}
