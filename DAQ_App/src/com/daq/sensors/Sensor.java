package com.daq.sensors;

import org.json.JSONObject;

import android.content.Context;
import com.idl.daq.SensorDetailFragment;

public abstract class Sensor {
	String sensorName,desc,formula;
	int id;
	SensorDetailFragment dataFrag;

	static private int sensorCount = 0;
	
	public Sensor(String sensorName,String formula) {
		super();
		this.sensorName = sensorName;
		this.formula = formula;
		sensorCount++;
		id = sensorCount;
		dataFrag = new SensorDetailFragment();
		//dataFrag.setRetainInstance(true);
		dataFrag.setSensor(this);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sensorName;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public int getId() {
		return id;
	}
	
	public abstract JSONObject getJSON();

	public SensorDetailFragment getDataFrag(Context c) {
		dataFrag.setContext(c);
		return dataFrag;
	}
	
	
	
	
	
}
